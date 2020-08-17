import { Controller, Get } from '@nestjs/common';
import { AppService } from './app.service';

@Controller()
export class AppController {

  constructor(
    private readonly appService: AppService
  ) {}

  @Get("notify")
  notify(): object {
    return {
      status: "ok"
    }
  }

  @Get("hang-up-notify")
  async hangUpNotify(): Promise<object> {
    return new Promise((resolve, _)=>{
      setTimeout(()=>resolve({status: "ok"}), 25000)
    })
  }

}
