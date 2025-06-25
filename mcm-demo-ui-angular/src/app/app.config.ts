import { HttpClientModule } from '@angular/common/http'; // <-- Agrega esta línea
import { ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';

import { provideRouter } from '@angular/router';
import { OAuthModule } from 'angular-oauth2-oidc';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes),
    importProvidersFrom(HttpClientModule),
    importProvidersFrom(OAuthModule.forRoot())
  ]
};