import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject } from 'rxjs';
import { authConfig } from './auth.config';

@Injectable({ providedIn: 'root' })
export class AuthService {

    private loggedIn$: BehaviorSubject<boolean>;

    constructor(private oauthService: OAuthService) {
        this.loggedIn$ = new BehaviorSubject<boolean>(this.oauthService.hasValidAccessToken());
        this.oauthService.configure(authConfig);
        this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
            this.loggedIn$.next(this.oauthService.hasValidAccessToken());
        });
        this.oauthService.events.subscribe(() => {
            this.loggedIn$.next(this.oauthService.hasValidAccessToken());
        });
    }

    login() {
        this.oauthService.initCodeFlow();
    }

    logout() {
        this.oauthService.logOut();
    }

    get token(): string {
        return this.oauthService.getAccessToken();
    }

    get isLoggedIn$() {
        return this.loggedIn$.asObservable();
    }

    get userProfile(): any {
        return this.oauthService.getIdentityClaims();
    }
}