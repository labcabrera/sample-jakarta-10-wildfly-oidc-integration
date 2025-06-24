import { Component, OnInit } from '@angular/core';
import { CustomerService } from './customer.service';

@Component({
  selector: 'app-customer-list',
  template: `<div *ngFor="let customer of customers">{{ customer.name }}</div>`
})
export class CustomerListComponent implements OnInit {
  customers: any[] = [];

  constructor(private customerService: CustomerService) {}

  ngOnInit() {
    //this.customerService.getCustomers().subscribe(data => this.customers = data['content']);
  }
}