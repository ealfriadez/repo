import { Component, inject, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { AuthService } from '../../core/services/auth.service';
import { BreadcrumbComponent } from '../../shared/breadcrumb/breadcrumb.component';


interface NavItem {
  label: string;
  icon: string;
  route: string;
  group?: string;
}

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, MatIconModule, MatButtonModule, MatMenuModule, MatBadgeModule, BreadcrumbComponent],
  templateUrl: './admin-layout.component.html',
  styleUrl: './admin-layout.component.css'
})
export class AdminLayoutComponent {
  auth = inject(AuthService);
  sidebarCollapsed = signal(false);
  sidebarOpen = signal(false);

  ownerNav: NavItem[] = [
    { label: 'Dashboard', icon: 'dashboard', route: '/owner/dashboard', group: 'Principal' },
    { label: 'Mis Libros', icon: 'menu_book', route: '/owner/books', group: 'Catalogo' },
    { label: 'Mis Autores', icon: 'person', route: '/owner/authors', group: 'Catalogo' },
    { label: 'Carga Masiva', icon: 'upload_file', route: '/owner/bulk-upload', group: 'Catalogo' },
    { label: 'Ventas', icon: 'bar_chart', route: '/owner/reports', group: 'Reportes' },
    { label: 'Ingesta IA', icon: 'cloud_upload', route: '/owner/ingestion', group: 'Herramientas' },
  ];

  get navItems(): NavItem[] {
    return this.ownerNav;
  }

  getGroups(): string[] {
    return [...new Set(this.ownerNav.map(i => i.group || ''))];
  }

  getItemsByGroup(group: string): NavItem[] {
    return this.ownerNav.filter(i => (i.group || '') === group);
  }

  toggleSidebar(): void {
    if (window.innerWidth <= 768) {
      this.sidebarOpen.update(v => !v);
    } else {
      this.sidebarCollapsed.update(v => !v);
    }
  }

  closeSidebar(): void {
    this.sidebarOpen.set(false);
  }
}
