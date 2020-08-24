import { Injectable, Logger } from "@nestjs/common";
import { AmqpInterceptor, IAmqpInterceptor } from "src/nestx-amqp/amqp.interceptor";
import { Tracer, FORMAT_HTTP_HEADERS } from "opentracing";



@AmqpInterceptor()
@Injectable()
export class TracingAmqpInterceptor implements IAmqpInterceptor {

  private readonly logger: Logger = new Logger("TracingAmqpInterceptor")

  constructor(
    private tracer: Tracer
  ){}

  async receive(message:any, next:(updateContent:any)=>Promise<any>){
    const parentSpanContext = this.tracer.extract(FORMAT_HTTP_HEADERS, message.properties.headers)
    this.logger.log("span init")
    const span = this.tracer.startSpan('amqp_interceptor', {
      childOf: parentSpanContext
    })
    return next(message)
    .then((result)=> {
      this.logger.log("span finish")
      span.finish()
      return result
    })
    .catch((reason)=> {
      this.logger.log("span finish with error")
      //TODO tag span
      span.finish()
      throw reason
    })
  }

  //work in progress
  async send(content:any, next){

  }
}
