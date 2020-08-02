import { Injectable, NestMiddleware } from '@nestjs/common';
import { Request, Response } from 'express';
import { Tracer, FORMAT_HTTP_HEADERS } from 'opentracing';

@Injectable()
export class TracingMiddleware implements NestMiddleware {

  constructor(
    private readonly tracer: Tracer
  ) {}

  use(req: Request, res: Response, next: Function) {
    const parentSpanContext = this.tracer.extract(FORMAT_HTTP_HEADERS, req.headers)
    console.log("span init")
    const span = this.tracer.startSpan('http_server', {
      childOf: parentSpanContext
    })
    res.on('finish', ()=>{
      span.finish()
      console.log("span end")
    }) 
    res.on('close', ()=>{
      span.finish()
    }) 
    res.on('error', ()=>{
      span.finish()
    }) 
    next();
  }
}
