import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
  issuer: 'http://127.0.0.1:8090/realms/mcm-demo',
  redirectUri: window.location.origin,
  clientId: 'mcm-demo-ui-angular',
  responseType: 'code',
  scope: 'openid profile email',
  showDebugInformation: true,
  strictDiscoveryDocumentValidation: false,

  // LOCAL CONFIG
  requireHttps: false
};
