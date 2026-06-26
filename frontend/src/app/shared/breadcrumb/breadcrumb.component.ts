import { Component, computed, inject } from '@angular/core';
import { Router, NavigationEnd, RouterLink } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';

interface Breadcrumb {
  label: string;
  url: string;
}

@Component({
  selector: 'app-breadcrumb',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './breadcrumb.component.html',
  styleUrl: './breadcrumb.component.css'
})
export class BreadcrumbComponent {
  private router = inject(Router);

  private url = toSignal(
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd),
      map((e: NavigationEnd) => e.urlAfterRedirects || e.url)
    ),
    { initialValue: this.router.url }
  );

  isVisible = computed(() => {
    const url = this.url();
    const hiddenRoutes = ['/', '/login', '/register'];
    return !hiddenRoutes.includes(url);
  });

  breadcrumbs = computed<Breadcrumb[]>(() => {
    const url = this.url();
    const segments = url.split('/').filter(s => s && !s.startsWith('?'));

    const routeLabels: Record<string, string> = {
      'catalog': 'Catalogo',
      'chat': 'Chat IA',
      'cart': 'Carrito',
      'my-purchases': 'Mis Compras',
      'books': 'Libros',
      'authors': 'Autores',
      'profile': 'Mi Perfil',
      'owner': 'Mi Libreria',
      'dashboard': 'Dashboard',
      'reports': 'Reportes',
      'bulk-upload': 'Carga Masiva',
      'ingestion': 'Ingesta',
      'my-books': 'Mis Libros',
      'change-password': 'Cambiar Contrasena',
      'reviews': 'Resenas'
    };

    const hiddenSegments = ['customer', 'owner', 'login', 'register'];
    const crumbs: Breadcrumb[] = [{ label: 'Inicio', url: '/catalog' }];
    let currentPath = '';

    for (const segment of segments) {
      currentPath += '/' + segment;
      if (hiddenSegments.includes(segment)) continue;
      const label = routeLabels[segment] || this.formatSegment(segment);
      crumbs.push({ label, url: currentPath });
    }

    return crumbs;
  });

  pageTitle = computed(() => {
    const crumbs = this.breadcrumbs();
    return crumbs.length > 0 ? crumbs[crumbs.length - 1].label : 'Inicio';
  });

  private formatSegment(segment: string): string {
    return segment
      .split('-')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }
}
