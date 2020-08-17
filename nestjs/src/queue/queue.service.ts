import { Injectable, Logger } from '@nestjs/common';
import { SubscribeQueue, UseAMQPConnection } from 'nestx-amqp';

@Injectable()
export class QueueService {

  logger: Logger = new Logger("QueueController")

  constructor() {
    
  }

  @UseAMQPConnection('OPENTRACING_QUEUE')
  @SubscribeQueue("opentracing")
  async notify(message: any, error: any): Promise<object> {
    this.logger.log(this)
    this.logger.log(message)
    return Promise.resolve({
      status: "ok"
    });
  }

}
