import { Component, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BookService } from '../../../core/services/book.service';
import { BulkUploadResponse } from '../../../models/book.model';

@Component({
  selector: 'app-book-bulk-upload',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './book-bulk-upload.component.html',
  styleUrl: './book-bulk-upload.component.css'
})
export class BookBulkUploadComponent {
  selectedFile = signal<File | null>(null);
  uploading = signal(false);
  result = signal<BulkUploadResponse | null>(null);
  isDragOver = signal(false);

  constructor(
    private bookService: BookService,
    private snackBar: MatSnackBar
  ) {}

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver.set(true);
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver.set(false);
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.isDragOver.set(false);
    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      this.selectedFile.set(files[0]);
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile.set(input.files[0]);
    }
  }

  clearFile(event: Event): void {
    event.stopPropagation();
    this.selectedFile.set(null);
    this.result.set(null);
  }

  formatFileSize(bytes: number): string {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1048576) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / 1048576).toFixed(1) + ' MB';
  }

  upload(): void {
    const file = this.selectedFile();
    if (!file) {
      this.snackBar.open('Seleccione un archivo', 'Cerrar', { duration: 3000 });
      return;
    }

    this.uploading.set(true);
    this.result.set(null);

    this.bookService.bulkUpload(file).subscribe({
      next: (res) => {
        this.result.set(res);
        this.uploading.set(false);
        this.snackBar.open(
          `Creados: ${res.created}, Actualizados: ${res.updated}, Errores: ${res.errors.length}`,
          'Cerrar',
          { duration: 5000 }
        );
      },
      error: () => {
        this.uploading.set(false);
      }
    });
  }

  reset(): void {
    this.selectedFile.set(null);
    this.result.set(null);
  }
}
