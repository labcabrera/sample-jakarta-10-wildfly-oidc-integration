import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-customer-list',
  imports: [],
  templateUrl: './customer-list.html',
  styleUrl: './customer-list.scss'
})
export class CustomerList implements OnInit {
  customers: any[] = [];

  //constructor(private customerService: CustomerService) { }

  ngOnInit() {
    this.customers = [
      {name: 'John Doe', email: 'jd@test.com'},
      {name: 'John Doe', email: 'jd@test.com'}
    ];

    // this.customerService.getCustomers().subscribe((data: any) => {
    //   this.customers = Array.isArray(data) ? data : [];
    // });
  }
}
