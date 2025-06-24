import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private apiUrl = 'http://localhost:8081/demo-api/api/customers';

  constructor(private http: HttpClient, private oauthService: OAuthService) {}

  getCustomers() {
    const token = this.oauthService.getAccessToken();
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);
    return this.http.get(this.apiUrl, { headers });
  }
}