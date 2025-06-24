import { Routes } from '@angular/router';
import { CustomerList } from './customer-list/customer-list';
import { Home } from './home/home';

export const routes: Routes = [
    { path: '', component: Home },
    { path: 'customers', component: CustomerList }
];