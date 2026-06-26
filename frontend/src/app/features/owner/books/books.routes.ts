import { Routes } from '@angular/router';
export const BOOKS_ROUTES: Routes = [
  { path: '', loadComponent: () => import('./books-list/books-list.component').then(m => m.BooksListComponent) },
  { path: 'new', loadComponent: () => import('./book-form/book-form.component').then(m => m.BookFormComponent) },
  { path: 'edit/:id', loadComponent: () => import('./book-form/book-form.component').then(m => m.BookFormComponent) }
];
