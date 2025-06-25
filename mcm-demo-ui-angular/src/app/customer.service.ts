import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class CustomerService {

    //TODO read from environment
    private apiUrl = 'http://mcm-demo-api-customers.local/api-customers/api/customers';

    constructor(private http: HttpClient, private auth: AuthService) { }

    searchCustomers(): Observable<any> {
        const token = this.auth.token;
        const headers = new HttpHeaders({
            Authorization: `Bearer ${token}`
        });
        return this.http.get<any>(this.apiUrl, { headers });
    }
}