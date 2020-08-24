
export interface IAmqpInterceptor{
  receive?(content:any, next:(updateContent:any)=>Promise<any>):Promise<void>;
  send?(content:any, next:(updateContent:any)=>Promise<any>):Promise<void>;
}


export interface AmqpInterceptorOptions{

}

export function AmqpInterceptor(): ClassDecorator
export function AmqpInterceptor(
  options?: AmqpInterceptorOptions
): ClassDecorator {
  return (target) => {
    Reflect.defineMetadata(
      'AMQP_INTERCEPTOR',
      true,
      target
    )
    Reflect.defineMetadata(
      'AMQP_INTERCEPTOR_OPTIONS',
      options,
      target
    )
    return target
  }
}
