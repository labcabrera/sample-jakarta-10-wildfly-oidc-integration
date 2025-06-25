import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from './auth.config';

@Injectable({ providedIn: 'root' })
export class AuthService {
    constructor(private oauthService: OAuthService) {
        this.oauthService.configure(authConfig);
        this.oauthService.loadDiscoveryDocumentAndTryLogin();
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

    get isLoggedIn(): boolean {
        return this.oauthService.hasValidAccessToken();
    }

    get userProfile(): any {
        return this.oauthService.getIdentityClaims();
    }
}
