import { Injectable, NestMiddleware, Logger } from '@nestjs/common';
import { Request, Response } from 'express';
import { Tracer, FORMAT_HTTP_HEADERS } from 'opentracing';

@Injectable()
export class TracingMiddleware implements NestMiddleware {

  logger: Logger = new Logger("TracingMiddleware")

  constructor(
    private readonly tracer: Tracer
  ) { }

  use(req: Request, res: Response, next: Function) {
    const parentSpanContext = this.tracer.extract(FORMAT_HTTP_HEADERS, req.headers)
    this.logger.log("span init")
    const span = this.tracer.startSpan('http_server', {
      childOf: parentSpanContext
    })
    res.on('finish', ()=>{
      span.finish()
      this.logger.log("span finish")
    })
    res.on('close', ()=>{
      span.finish()
      this.logger.log("span finish on close")
    })
    res.on('error', ()=>{
      span.finish()
      this.logger.log("span finish on error")
    })
    next();
  }
}
