import { Module, NestModule, MiddlewareConsumer } from '@nestjs/common';
import { TracingMiddleware } from './tracing.middleware';
import { Tracer } from 'opentracing';
import { SharedModule } from 'src/shared/shared.module';

@Module({
  imports: [
    SharedModule
  ]
})
export class TracingModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    consumer
      .apply(TracingMiddleware)
      .forRoutes("*")
  }
}
