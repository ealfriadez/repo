import { Routes } from '@angular/router';
export const INGESTION_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./ingestion-page/ingestion-page.component').then(m => m.IngestionPageComponent) }
];
