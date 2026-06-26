import { Routes } from '@angular/router';
export const SALES_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./sales-list/sales-list.component').then(m => m.SalesListComponent) },
  { path: 'new', loadComponent: () => import('./sale-form/sale-form.component').then(m => m.SaleFormComponent) }
];
