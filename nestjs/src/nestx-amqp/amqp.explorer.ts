import { DiscoveryService, DiscoveredClassWithMeta } from '@golevelup/nestjs-discovery'
import { Injectable, Logger, OnModuleInit, Inject } from '@nestjs/common'
import { ModuleRef } from '@nestjs/core'
import { AmqpConnectionManager } from 'amqp-connection-manager'
import {
  PUBLISH_EXCHANGE_CONTEXT_METADATA_TOKEN,
  PUBLISH_EXCHANGE_METADATA_TOKEN,
  PUBLISH_EXCHANGE_PRODUCER_METADATA_TOKEN,
  PUBLISH_QUEUE_CONTEXT_METADATA_TOKEN,
  PUBLISH_QUEUE_METADATA_TOKEN,
  PUBLISH_QUEUE_PRODUCER_METADATA_TOKEN,
  SUBSCRIBE_QUEUE_CONSUME_OPTIONS_METADATA_TOKEN,
  SUBSCRIBE_QUEUE_CONSUMER_METADATA_TOKEN,
  SUBSCRIBE_QUEUE_CONTEXT_METADATA_TOKEN,
  SUBSCRIBE_QUEUE_METADATA_TOKEN,
  USE_AMQP_CONNECTION_TOKEN,
} from 'nestx-amqp'
import { Exchange } from 'nestx-amqp'
import { ConsumeQueueOptions, Queue } from 'nestx-amqp'
import { Consumer } from './consumer'
import { ExchangeProducer } from 'nestx-amqp'
import { QueueProducer } from 'nestx-amqp'
import { getAMQPConnectionToken } from './token.util'
import { AmqpInterceptorOptions } from './amqp.interceptor'


@Injectable()
export class AMQPExplorer implements OnModuleInit {

  private readonly logger = new Logger(AMQPExplorer.name)

  private amqpInterceptorProviders: DiscoveredClassWithMeta<AmqpInterceptorOptions>[]

  constructor(
    private readonly moduleRef: ModuleRef,
    private readonly discoveryService: DiscoveryService
  ) {}

  async onModuleInit() {
    await this.explore()
  }

  async explore() {
    await this.discoveryAmpqInterceptorClasses()
    await this.registerPublishQueueMethods()
    await this.registerPublishExchangeMethods()
    await this.registerSubscribeQueueMethods()
  }


  private async discoveryAmpqInterceptorClasses() {
    const amqpInterceptorProviders = await this.discoveryService.providersWithMetaAtKey<AmqpInterceptorOptions>(
      'AMQP_INTERCEPTOR'
    );
    this.amqpInterceptorProviders = amqpInterceptorProviders
    for (const amqpInterceptorProvider of amqpInterceptorProviders) {
      const handlerContext = amqpInterceptorProvider.discoveredClass.instance
      const className = amqpInterceptorProvider.discoveredClass.name;
      this.logger.log(
        `Found ${className} using @AmpqInterceptor()`
      )
    }
  }

  /**
   * @desc discover all providers' method with `@PublishQueue`, create producer instance
   * */
  private async registerPublishQueueMethods() {
    /** scan all `@PublishQueue` methods */
    const publishQueueMethods = await this.discoveryService.providerMethodsWithMetaAtKey(PUBLISH_QUEUE_METADATA_TOKEN)

    /** create all queue producer with spec connection if have `@UseConnection` */
    for (const method of publishQueueMethods) {
      const originalHandler = method.discoveredMethod.handler

      const connectionName = Reflect.getMetadata(USE_AMQP_CONNECTION_TOKEN, originalHandler)
      const injectConnectionToken = getAMQPConnectionToken(connectionName)
      const connection: AmqpConnectionManager = this.moduleRef.get<AmqpConnectionManager>(injectConnectionToken)
      const queue: Queue = Reflect.getMetadata(PUBLISH_QUEUE_METADATA_TOKEN, originalHandler)
      const handlerContext = method.discoveredMethod.parentClass.instance

      const producer = new QueueProducer(connection, queue)
      await producer.onModuleInit()

      Reflect.defineMetadata(PUBLISH_QUEUE_PRODUCER_METADATA_TOKEN, producer, originalHandler)
      Reflect.defineMetadata(PUBLISH_QUEUE_CONTEXT_METADATA_TOKEN, handlerContext, originalHandler)

      const className = method.discoveredMethod.parentClass.name;
      const methodName = method.discoveredMethod.methodName;
      this.logger.log(
        `Found ${className}#${methodName} using @PublishQueue()`
      )
    }
  }


  private async registerPublishExchangeMethods() {
    const publishExchangeMethods = await this.discoveryService.providerMethodsWithMetaAtKey(
      PUBLISH_EXCHANGE_METADATA_TOKEN
    )

    for (const method of publishExchangeMethods) {
      const originalHandler = method.discoveredMethod.handler

      const connectionName = Reflect.getMetadata(USE_AMQP_CONNECTION_TOKEN, originalHandler)
      const injectConnectionToken = getAMQPConnectionToken(connectionName)
      const connection: AmqpConnectionManager = this.moduleRef.get<AmqpConnectionManager>(injectConnectionToken)
      const exchange: Exchange = Reflect.getMetadata(PUBLISH_EXCHANGE_METADATA_TOKEN, originalHandler)
      const handlerContext = method.discoveredMethod.parentClass.instance

      const producer = new ExchangeProducer(connection, exchange)
      await producer.onModuleInit()

      Reflect.defineMetadata(PUBLISH_EXCHANGE_PRODUCER_METADATA_TOKEN, producer, originalHandler)
      Reflect.defineMetadata(PUBLISH_EXCHANGE_CONTEXT_METADATA_TOKEN, handlerContext, originalHandler)
      const className = method.discoveredMethod.parentClass.name;
      const methodName = method.discoveredMethod.methodName;
      this.logger.log(
        `Found ${className}#${methodName} using @PublishExchange()`
      )
    }
  }

  private async registerSubscribeQueueMethods() {
    const subscribeQueueMethods = await this.discoveryService.providerMethodsWithMetaAtKey(
      SUBSCRIBE_QUEUE_METADATA_TOKEN
    )
    for (const method of subscribeQueueMethods) {
      const originalHandler = method.discoveredMethod.handler
      const connectionName = Reflect.getMetadata(
        USE_AMQP_CONNECTION_TOKEN,
        originalHandler
      )
      const injectConnectionToken = getAMQPConnectionToken(connectionName)
      const connection: AmqpConnectionManager = this.moduleRef
      .get<AmqpConnectionManager>(injectConnectionToken)
      const queue: Queue = Reflect.getMetadata(
        SUBSCRIBE_QUEUE_METADATA_TOKEN,
        originalHandler
      )
      const consumeOptions: ConsumeQueueOptions = Reflect.getMetadata(
        SUBSCRIBE_QUEUE_CONSUME_OPTIONS_METADATA_TOKEN,
        originalHandler
      )
      const handlerContext = method.discoveredMethod.parentClass.instance
      const consumer = new Consumer(connection, queue, consumeOptions)
      await consumer.applyHandler(originalHandler)
      await consumer.applyContext(handlerContext)
      await consumer.applyInterceptorProviders(this.amqpInterceptorProviders)
      Reflect.defineMetadata(
        SUBSCRIBE_QUEUE_CONSUMER_METADATA_TOKEN,
        consumer,
        originalHandler
      )
      Reflect.defineMetadata(
        SUBSCRIBE_QUEUE_CONTEXT_METADATA_TOKEN,
        handlerContext,
        originalHandler
      )
      const className = method.discoveredMethod.parentClass.name;
      const methodName = method.discoveredMethod.methodName;
      this.logger.log(
        `Found ${className}#${methodName} using @SubscribeQueue()`
      )
      await consumer.listen()
    }
  }
}
