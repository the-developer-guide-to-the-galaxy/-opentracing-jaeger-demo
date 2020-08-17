import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { Logger } from '@nestjs/common';
import { Transport } from '@nestjs/microservices';

const PORT = process.env.PORT || 3000

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  // app.connectMicroservice({
  //   transport: Transport.RMQ,
  //   options: {
  //     urls: [`amqp://guest:guest@192.168.0.31:5672`],
  //     queue: 'my_queue',
  //     queueOptions: {
  //       durable: false
  //     }
  //   }
  // });
  await app.startAllMicroservicesAsync();
  await app.listen(PORT);
  const logger = new Logger("Bootstrap");
  logger.log("App listening on port " + PORT)
}
bootstrap();
