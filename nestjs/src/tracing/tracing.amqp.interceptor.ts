import { Injectable } from "@nestjs/common";
import { AmqpInterceptor, IAmqpInterceptor } from "src/nestx-amqp/amqp.interceptor";



@AmqpInterceptor()
@Injectable()
export class TracingAmqpInterceptor implements IAmqpInterceptor {
  async receive(content:any, next){
    return next(content)
  }
  async send(content:any, next){

  }
}
