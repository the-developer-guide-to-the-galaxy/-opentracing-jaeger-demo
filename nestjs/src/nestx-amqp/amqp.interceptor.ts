
export interface IAmqpInterceptor{
  receive?(message:any, next:(updateContent:any)=>Promise<any>):Promise<void>;
  send?(message:any, next:(updateContent:any)=>Promise<any>):Promise<void>;
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
