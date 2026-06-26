import { Routes } from '@angular/router';
export const REVIEWS_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./review-page/review-page.component').then(m => m.ReviewPageComponent) },
  { path: 'new', loadComponent: () => import('./review-form/review-form.component').then(m => m.ReviewFormComponent) },
  { path: 'edit/:id', loadComponent: () => import('./review-form/review-form.component').then(m => m.ReviewFormComponent) }
];
