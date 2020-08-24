import { Module } from '@nestjs/common';
import { QueueService } from './queue.service';
//import { AMQPModule } from 'nestx-amqp'
import { AMQPModule } from 'src/nestx-amqp/amqp.module'

@Module({
  imports: [
    AMQPModule.register({
      name: 'OPENTRACING_QUEUE',
      urls: ['amqp://guest:guest@localhost:5672']
    })
  ],
  providers: [
    QueueService
  ]
})
export class QueueModule {

}
