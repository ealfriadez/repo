import { Component, inject, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { IngestionService } from '../../../../core/services/ingestion.service';

@Component({
  selector: 'app-ingestion-page',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, MatIconModule, MatProgressBarModule, MatSnackBarModule],
  templateUrl: './ingestion-page.component.html',
  styleUrl: './ingestion-page.component.css'
})
export class IngestionPageComponent {
  private ingestionService = inject(IngestionService);
  private snackBar = inject(MatSnackBar);

  loading = signal(false);
  result = signal('');

  ingestBooks(): void {
    this.loading.set(true);
    this.result.set('');
    this.ingestionService.ingestBooks().subscribe({
      next: (res) => {
        this.loading.set(false);
        this.result.set(`${res.message} — ${res.totalIndexed} libros indexados`);
        this.snackBar.open(this.result(), 'Cerrar', { duration: 5000 });
      },
      error: () => {
        this.loading.set(false);
        this.snackBar.open('Error al indexar libros', 'Cerrar', { duration: 3000 });
      }
    });
  }
}
