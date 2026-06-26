import { Routes } from '@angular/router';
export const CATALOG_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./catalog-page/catalog-page.component').then(m => m.CatalogPageComponent) },
  { path: ':slug', loadComponent: () => import('./book-detail/book-detail.component').then(m => m.BookDetailComponent) }
];
