import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {RxStompService} from "./rx-stomp.service";
import {RxStompServiceFactory} from "./rxStompServiceFactory";
import {provideHttpClient} from "@angular/common/http";

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    {
      provide: RxStompService,
      useFactory: RxStompServiceFactory,
    }
  ]
};
