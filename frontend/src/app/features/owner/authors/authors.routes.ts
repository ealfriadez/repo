import { Routes } from '@angular/router';
export const AUTHORS_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./authors-list/authors-list.component').then(m => m.AuthorsListComponent) },
  { path: 'new', loadComponent: () => import('./author-form/author-form.component').then(m => m.AuthorFormComponent) },
  { path: 'edit/:id', loadComponent: () => import('./author-form/author-form.component').then(m => m.AuthorFormComponent) }
];
