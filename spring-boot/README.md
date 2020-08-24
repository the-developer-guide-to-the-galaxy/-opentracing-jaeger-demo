#### endpoint 1 
#### HttpFullTrace

This endpoint generates complete trace, correct reporting depends on reporter configuration (change endpoint for testing)

```sh
curl 'http://localhost:8080/http-full-trace' | python -m json.tool
```

#### endpoint 2 
#### HttpCustomRestTemplateTrace

This endpoint generates incomplete trace due custom non-traced http client

```sh
curl 'http://localhost:8080/http-trace-custom-rest-template' | python -m json.tool
```

Inspect below classes should be useful to understand what is the problem 

```ini
# manage restTemplate instance creation
org.springframework.boot.web.client.RestTemplateBuilder

# declare RestTemplateBuilder instance
# inject restTemplateCustomizer list
org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration

# declare TracingRestTemplateCustomizer instance
io.opentracing.contrib.spring.web.starter.RestTemplateTracingAutoConfiguration

# add TracingRestTemplateInterceptor to RestTemplate interceptor set
io.opentracing.contrib.spring.web.starter.client.TracingRestTemplateCustomizer
```

#### endpoint 3
#### HttpAsyncBrokenTrace

This endpoint generates new unrelated trace for each http request

```sh
curl 'http://localhost:8080/http-async-broken-trace' | python -m json.tool
```

Inspect below classes should be useful to understand what is the problem 

```ini
# manage trace state
io.opentracing.util.ThreadLocalScopeManager

# trace state
io.opentracing.util.ThreadLocalScope
```

#### endpoint 4 
#### HttpAsyncFixedTrace

This endpoint generates asynchronous trace for http requests

```sh
curl 'http://localhost:8080/http-async-fixed-trace' | python -m json.tool
```

Inspect below classes should be useful to understand what is the problem 

```ini
io.opentracing.contrib.concurrent.TracedExecutorService
```


#### endpoint 5 
#### HttpAsyncHangUpTrace

This endpoint hangs up over another service. 

```sh
curl 'http://localhost:8080/http-async-fixed-trace' | python -m json.tool
```

#### endpoint 6 
#### HttpAsyncHystrixTrace

This endpoint makes use of Hystrix command for actions timeout. 

```sh
curl 'http://localhost:8080/http-async-hystrix-trace' | python -m json.tool
```

depends on `TracedHystrixConcurrencyStrategy` registry to connect trace between services

#### endpoint 7 
#### AmqpAsyncBrokenTrace

This endpoint generates disconnected trace though amqp messaging. 

```sh
curl 'http://localhost:8080/amqp-async-broken-trace' | python -m json.tool
```

depends on `TracedHystrixConcurrencyStrategy` registry to connect trace between services


#### endpoint 8 
#### AmqpAsyncFixedTrace


This endpoint generates disconnected trace though amqp messaging. 

```sh
curl 'http://localhost:8080/amqp-async-fixed-trace' | python -m json.tool
```

depends on `TracedHystrixConcurrencyStrategy` registry to connect trace between services

io.opentracing.contrib.spring.rabbitmq.RabbitMqTracingAutoConfiguration

https://github.com/opentracing-contrib/java-spring-rabbitmq

org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration





