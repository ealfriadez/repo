import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { BookService } from '../../../../core/services/book.service';
import { AuthorService } from '../../../../core/services/author.service';
import { BookRequest, AuthorResponse } from '../../../../models/api.models';

@Component({
  selector: 'app-book-form',
  standalone: true,
  imports: [
    ReactiveFormsModule, RouterLink,
    MatCardModule, MatFormFieldModule, MatInputModule,
    MatSelectModule, MatButtonModule, MatIconModule, MatSnackBarModule
  ],
  templateUrl: './book-form.component.html',
  styleUrl: './book-form.component.css'
})
export class BookFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private bookService = inject(BookService);
  private authorService = inject(AuthorService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private snackBar = inject(MatSnackBar);

  isEditMode = signal(false);
  authors = signal<AuthorResponse[]>([]);
  selectedImage: File | null = null;
  imagePreview = signal<string | null>(null);
  selectedBookFile = signal<File | null>(null);
  private bookId: number | null = null;

  bookForm = this.fb.group({
    title: ['', Validators.required],
    authorId: [0, [Validators.required, Validators.min(1)]],
    genre: ['', Validators.required],
    description: [''],
    price: [0, [Validators.required, Validators.min(0.01)]],
    stock: [0, [Validators.required, Validators.min(0)]],
    minStock: [5, Validators.min(0)]
  });

  slugPreview = computed(() => {
    const title = this.bookForm.get('title')?.value ?? '';
    const authorId = this.bookForm.get('authorId')?.value ?? 0;
    const author = this.authors().find(a => a.id === authorId);
    if (!title || !author) return '';
    return this.generateSlug(title, `${author.firstName} ${author.lastName}`);
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    // Primero carga autores, luego si es edición carga el libro
    this.authorService.findAll(0, 100).subscribe({
      next: (page) => {
        this.authors.set(page.content);

        if (id) {
          this.isEditMode.set(true);
          this.bookId = +id;
          this.bookService.findById(+id).subscribe({
            next: (book) => {
              const author = page.content.find(a => `${a.firstName} ${a.lastName}` === book.authorFullName);
              this.bookForm.patchValue({
                title: book.title,
                genre: book.genre,
                price: book.price,
                stock: book.stock,
                authorId: author?.id ?? 0,
                description: book.description
              });
            },
            error: () => {}
          });
        }
      },
      error: () => {}
    });
  }

  private generateSlug(title: string, authorName: string): string {
    return (title + ' ' + authorName)
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase()
      .replace(/[^a-z0-9\s-]/g, '')
      .trim()
      .replace(/\s+/g, '-')
      .replace(/-+/g, '-')
      .replace(/^-|-$/g, '');
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedImage = input.files[0];
      const reader = new FileReader();
      reader.onload = () => this.imagePreview.set(reader.result as string);
      reader.readAsDataURL(this.selectedImage);
    }
  }

  removeImage(event: Event): void {
    event.stopPropagation();
    this.selectedImage = null;
    this.imagePreview.set(null);
  }

  onBookFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedBookFile.set(input.files[0]);
    }
  }

  removeBookFile(event: Event): void {
    event.stopPropagation();
    this.selectedBookFile.set(null);
  }

  formatFileSize(bytes: number): string {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1048576) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / 1048576).toFixed(1) + ' MB';
  }

  saveBook(): void {
    if (this.bookForm.invalid) return;
    const formValue = this.bookForm.getRawValue();
    const request: BookRequest = {
      title: formValue.title ?? '',
      genre: formValue.genre ?? '',
      price: formValue.price ?? 0,
      stock: formValue.stock ?? 0,
      minStock: formValue.minStock ?? 5,
      authorId: formValue.authorId ?? 0,
      description: formValue.description ?? '',
      slug: this.slugPreview()
    };
    if (this.isEditMode() && this.bookId) {
      this.bookService.update(this.bookId, request, this.selectedImage ?? undefined, this.selectedBookFile() ?? undefined).subscribe({
        next: () => {
          this.snackBar.open('Libro actualizado correctamente', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/owner/books']);
        },
        error: () => {}
      });
    } else {
      this.bookService.create(request, this.selectedImage ?? undefined, this.selectedBookFile() ?? undefined).subscribe({
        next: () => {
          this.snackBar.open('Libro creado correctamente', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/owner/books']);
        },
        error: () => {}
      });
    }
  }
}
