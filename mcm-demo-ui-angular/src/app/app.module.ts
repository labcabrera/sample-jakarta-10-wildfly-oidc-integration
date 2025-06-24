import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OAuthModule } from 'angular-oauth2-oidc';
import { routes } from './app.routes';

@NgModule({
    imports: [
        RouterModule.forRoot(routes),
        OAuthModule.forRoot()
    ],
})
export class AppModule { }