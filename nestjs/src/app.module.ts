import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { TracingModule } from './tracing/tracing.module';
import { QueueModule } from './queue/queue.module';

@Module({
  imports: [
    TracingModule,
    QueueModule
  ],
  controllers: [
    AppController
  ],
  providers: [
    AppService
  ],
})
export class AppModule {}
