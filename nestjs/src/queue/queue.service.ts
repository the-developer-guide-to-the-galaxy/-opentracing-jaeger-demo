import { Injectable, Logger } from '@nestjs/common';
import { SubscribeQueue, UseAMQPConnection } from 'nestx-amqp';

@Injectable()
export class QueueService {

  logger: Logger = new Logger("QueueService")

  constructor() {

  }

  @UseAMQPConnection('OPENTRACING_QUEUE')
  @SubscribeQueue("opentracing")
  async handleOpentracing(content: any, properties: any): Promise<object> {
    this.logger.log(content)
    return Promise.resolve({
      status: "ok"
    });
  }

}
