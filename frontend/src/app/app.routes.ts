import { Routes } from '@angular/router';
import { authGuard, noAuthGuard } from './core/guards/auth.guard';
import { ROLES } from './core/constants/roles';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';

export const routes: Routes = [
  // ─── Auth (sin layout, solo para no logueados) ───
  { path: 'login', loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent), canActivate: [noAuthGuard] },
  { path: 'register', loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent), canActivate: [noAuthGuard] },

  // ─── Catalogo publico (guest + customer) ───
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: '', redirectTo: 'catalog', pathMatch: 'full' },
      { path: 'catalog', loadChildren: () => import('./features/customer/catalog/catalog.routes').then(m => m.CATALOG_ROUTES) },
    ]
  },

  // ─── Customer (requiere login como CUSTOMER) ───
  {
    path: 'customer',
    component: MainLayoutComponent,
    canActivate: [authGuard(ROLES.CUSTOMER)],
    children: [
      { path: 'cart', loadComponent: () => import('./features/customer/cart/cart.component').then(m => m.CartComponent) },
      { path: 'chat', loadChildren: () => import('./features/customer/chat/chat.routes').then(m => m.CHAT_ROUTES) },
      { path: 'my-purchases', loadChildren: () => import('./features/customer/sales/sales.routes').then(m => m.SALES_ROUTES) },
      { path: 'my-books', loadComponent: () => import('./features/customer/my-books/my-books.component').then(m => m.MyBooksComponent) },
      { path: 'profile', loadComponent: () => import('./features/customer/profile/customer-profile.component').then(m => m.CustomerProfileComponent) },
      { path: 'reviews', loadChildren: () => import('./features/customer/reviews/reviews.routes').then(m => m.REVIEWS_ROUTES) },
    ]
  },

  // ─── Owner (requiere login como OWNER) ───
  {
    path: 'owner',
    component: AdminLayoutComponent,
    canActivate: [authGuard(ROLES.OWNER)],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', loadComponent: () => import('./features/owner/dashboard/owner-dashboard.component').then(m => m.OwnerDashboardComponent) },
      { path: 'books', loadChildren: () => import('./features/owner/books/books.routes').then(m => m.BOOKS_ROUTES) },
      { path: 'authors', loadChildren: () => import('./features/owner/authors/authors.routes').then(m => m.AUTHORS_ROUTES) },
      { path: 'bulk-upload', loadComponent: () => import('./features/owner/bulk-upload/book-bulk-upload.component').then(m => m.BookBulkUploadComponent) },
      { path: 'reports', loadComponent: () => import('./features/owner/reports/owner-reports.component').then(m => m.OwnerReportsComponent) },
      { path: 'ingestion', loadChildren: () => import('./features/owner/ingestion/ingestion.routes').then(m => m.INGESTION_ROUTES) },
      { path: 'change-password', loadComponent: () => import('./features/owner/change-password/change-password.component').then(m => m.ChangePasswordComponent) },
    ]
  },

  // ─── Wildcard ───
  { path: '**', redirectTo: '/catalog' }
];
