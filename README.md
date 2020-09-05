# OpenTracing & Jaeger demo

This is a demo of **OpenTracing** and **Jaeger** technologies through multiple languages applications

https://opentracing.io    
https://www.jaegertracing.io

## Jaeger all-in-one

It is used to collect and inspect traces and spans

Run **Jaeger** *all-in-one* via **Docker**

```sh
docker run -d --rm \
  -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  -p 14250:14250 \
  -p 9411:9411 \
  --name opentracing-jaeger \
  jaegertracing/all-in-one:1.18
```

You shoud see **Jaeger**'s web on localhost's port 16686

## Rabbit

RabbitMQ is an open source message broker.

https://www.rabbitmq.com/

It is used to add reliability to asynchronous events

Run **Jaeger** *all-in-one* via **Docker**

```sh
docker run -d --rm \
  -d --hostname opentracing-rabbit \
  -p 4369:4369 \
  -p 5671:5671 \
  -p 5672:5672 \
  -p 25672:25672 \
  -p 15671:15671 \
  -p 15672:15672 \
  --name opentracing-rabbit \
  rabbitmq:3-management
```

You shoud see **Rabbit**'s web console on localhost's port 15672


If you are running this demo on Windows maybe you should use docker-machine IP address to connect applications and containers, which is usually `192.168.99.100`

Java
```ini
JAEGER_SERVICE_NAME=springboot
JAEGER_AGENT_HOST=localhost
JAEGER_AGENT_PORT=6831 # compact encoding
JAEGER_REPORTER_LOG_SPANS=false
AMQP_HOST=localhost
AMQP_PASSWORD=guest
AMQP_PORT=5672
AMQP_USERNAME=guest
HOSTS=http://localhost:3000,http://localhost:5000
HYSTRIX_STRATEGY_ENABLED=false
```

Netcore
```ini
JAEGER_SERVICE_NAME=netcore
JAEGER_AGENT_HOST=localhost
JAEGER_AGENT_PORT=6831 # compact encoding
```

Nest.js
```ini
JAEGER_SERVICE_NAME=nest.js
JAEGER_AGENT_HOST=localhost
JAEGER_AGENT_PORT=6832  # thrift-binary encoding
JAEGER_REPORTER_LOG_SPANS=false
```

The application use **Sring Boot** as entry point. The available endpoints can be seen in project's [README](spring-boot/README.md)
Alternatively there is a graphic [gallery](assets/gallery/diagram-1-a.md) that can help to uinderstand every aspect of this demo