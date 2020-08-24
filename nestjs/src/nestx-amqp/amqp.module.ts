import { DiscoveryModule } from '@golevelup/nestjs-discovery'
import { ModuleRef } from '@nestjs/core'
import { DynamicModule, Module, OnModuleDestroy, OnModuleInit } from '@nestjs/common'
import { AMQPContainer } from './amqp.container'
import { AMQPExplorer } from './amqp.explorer'
import { getAMQPConnectionOptionsToken, getAMQPConnectionToken } from './token.util'
import { createAMQPConnection, createAsyncAMQPConnectionOptions } from 'nestx-amqp'
import { AMQPAsyncConnectionOptions, AMQPConnectionOptions } from 'nestx-amqp'

@Module({
  imports: [
    DiscoveryModule
  ],
  providers:[]
})
export class AMQPModule implements OnModuleInit, OnModuleDestroy {
  
  constructor(private readonly moduleRef: ModuleRef) {}

  static register(options: AMQPConnectionOptions): DynamicModule {
    return {
      module: AMQPModule,
      providers: [
        AMQPExplorer,
        {
          provide: getAMQPConnectionOptionsToken(options.name),
          useValue: options,
        },
        createAMQPConnection(options.name),
      ],
      exports: [getAMQPConnectionToken(options.name)],
    }
  }

  static forRootAsync(options: AMQPAsyncConnectionOptions): DynamicModule {
    return {
      module: AMQPModule,
      imports: options.imports,
      providers: [AMQPExplorer, createAsyncAMQPConnectionOptions(options), createAMQPConnection(options.name)],
      exports: [getAMQPConnectionToken(options.name)],
    }
  }

  async onModuleInit() {}

  async onModuleDestroy() {
    await AMQPContainer.getInstance().clearAndShutdown()
  }
}
