import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthorService } from '../../../../core/services/author.service';
import { AuthorResponse } from '../../../../models/api.models';

@Component({
  selector: 'app-authors-list',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule
  ],
  templateUrl: './authors-list.component.html',
  styleUrl: './authors-list.component.css'
})
export class AuthorsListComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authorService = inject(AuthorService);
  private snackBar = inject(MatSnackBar);

  authors = signal<AuthorResponse[]>([]);
  totalElements = signal(0);
  pageSize = signal(10);
  pageIndex = signal(0);

  searchForm = this.fb.group({
    search: ['']
  });

  displayedColumns = ['id', 'firstName', 'lastName', 'nationality', 'actions'];

  ngOnInit(): void {
    this.loadAuthors();
  }

  onSearch(): void {
    this.pageIndex.set(0);
    this.loadAuthors();
  }

  loadAuthors(): void {
    const search = this.searchForm.get('search')?.value?.trim() || undefined;
    this.authorService.findAll(this.pageIndex(), this.pageSize(), search).subscribe({
      next: (page) => {
        this.authors.set(page.content);
        this.totalElements.set(page.totalElements);
      },
      error: () => {}
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadAuthors();
  }

  deleteAuthor(id: number): void {
    if (!confirm('¿Está seguro de eliminar este autor?')) return;
    this.authorService.delete(id).subscribe({
      next: () => {
        this.snackBar.open('Autor eliminado correctamente', 'Cerrar', { duration: 3000 });
        this.loadAuthors();
      },
      error: () => {}
    });
  }
}
