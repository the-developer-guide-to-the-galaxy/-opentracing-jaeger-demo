

Run **Jaeger** *all-in-one* via **Docker**

```sh
docker run -d --rm --name jaeger \
  -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  -p 14250:14250 \
  -p 9411:9411 \
  jaegertracing/all-in-one:1.18
```

You can inspect Jaeger web on localhost's port 16686

Use `ifconfig` to get local IP address. Host IP address is needed as env var (in my case IP address is 192.168.0.31)

Java
```ini
JAEGER_SERVICE_NAME=springboot
JAEGER_AGENT_HOST=192.168.0.31
JAEGER_AGENT_PORT=6831 # compact encoding
JAEGER_REPORTER_LOG_SPANS=false
```

Netcore
```ini
JAEGER_SERVICE_NAME=netcore
JAEGER_AGENT_HOST=192.168.0.31
JAEGER_AGENT_PORT=6831 # compact encoding
```

Nest.js
```ini
JAEGER_SERVICE_NAME=nest.js
JAEGER_AGENT_HOST=192.168.0.31
JAEGER_AGENT_PORT=6832  # thrift-binary encoding
JAEGER_REPORTER_LOG_SPANS=false
```
