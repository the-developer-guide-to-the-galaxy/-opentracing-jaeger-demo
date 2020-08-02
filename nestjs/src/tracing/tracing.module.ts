import { Module, NestModule, MiddlewareConsumer } from '@nestjs/common';
import { TracingMiddleware } from './tracing.middleware';
import { initTracer, TracingConfig } from 'jaeger-client';
import { Tracer, initGlobalTracer } from 'opentracing';

@Module({
  imports: [

  ],
  controllers: [
    
  ],
  providers: [
    {
      provide: Tracer,
      useFactory(): Tracer {
        var config: TracingConfig = {
            serviceName: process.env.JAEGER_SERVICE_NAME,
            sampler: {
              type: "const",
              param: 1
            },
            reporter: {
              agentHost: process.env.JAEGER_AGENT_HOST,
              agentPort: parseInt(process.env.JAEGER_AGENT_PORT),
              logSpans: process.env.JAEGER_REPORTER_LOG_SPANS == 'true',
            }
        };
        var options = {
            
        };
        var tracer = initTracer(config, options);
        initGlobalTracer(tracer);
        return tracer;
      }
    }
  ]
})
export class TracingModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    consumer
      .apply(TracingMiddleware)
      .forRoutes("*")
  }
}
