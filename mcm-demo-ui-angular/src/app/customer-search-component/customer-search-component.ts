import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-customer-search-component',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule],
  templateUrl: './customer-search-component.html',
  styleUrl: './customer-search-component.scss'
})
export class CustomerSearchComponent {

  customers: any[] = [];
  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'email'];

  constructor(private customerService: CustomerService) { }

  buscar() {
    this.customerService.searchCustomers().subscribe(data => {
      this.customers = data.content;
    });
  }
}
