import { Routes } from '@angular/router';
import { CustomerSearchComponent } from './customer-search-component/customer-search-component';
import { App } from './app';

export const routes: Routes = [
    {
        path: '', component: App
    },
    {
        path: 'customers', component: CustomerSearchComponent
    }
];
