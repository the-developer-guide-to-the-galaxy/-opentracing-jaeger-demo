import { Injectable, Logger } from '@nestjs/common';
import { SubscribeQueue, UseAMQPConnection } from 'nestx-amqp';

@Injectable()
export class QueueService {

  logger: Logger = new Logger("QueueService")

  constructor() {

  }

  @UseAMQPConnection('OPENTRACING_QUEUE')
  @SubscribeQueue("opentracing")
  async handleOpentracing(message: any): Promise<object> {
    this.logger.log(message)
    return Promise.resolve({
      status: "ok"
    });
  }

}
