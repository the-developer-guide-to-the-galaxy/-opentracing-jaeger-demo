## **OpenTracing** **Jaeger** implementation over **Nest.js** framework 

<p align="left">
  <a href="http://nestjs.com/" target="blank"><img src="https://nestjs.com/img/logo_text.svg" width="25%" alt="Nest Logo" /></a>
</p>

#### Installation

```bash
$ npm install
```

#### Configure the app

```
export JAEGER_SERVICE_NAME=nest.js \
export JAEGER_AGENT_HOST=localhost \
export JAEGER_AGENT_PORT=6832  # thrift-binary encoding \
export JAEGER_REPORTER_LOG_SPANS=false
```

#### Running the app

```bash
# development
$ npm run start

# watch mode
$ npm run start:dev

# production mode
$ npm run start:prod
```

#### Test

```bash
# unit tests
$ npm run test

# e2e tests
$ npm run test:e2e

# test coverage
$ npm run test:cov
```

#### License

  Nest is [MIT licensed](https://github.com/nestjs/nest/blob/master/LICENSE).
