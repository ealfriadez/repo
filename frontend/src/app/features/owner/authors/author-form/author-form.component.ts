import { Component, OnInit, inject, signal } from '@angular/core';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthorService } from '../../../../core/services/author.service';
import { AuthorRequest } from '../../../../models/api.models';

@Component({
  selector: 'app-author-form',
  standalone: true,
  imports: [
    ReactiveFormsModule, RouterLink,
    MatCardModule, MatFormFieldModule, MatInputModule,
    MatButtonModule, MatIconModule, MatSnackBarModule
  ],
  templateUrl: './author-form.component.html',
  styleUrl: './author-form.component.css'
})
export class AuthorFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authorService = inject(AuthorService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private snackBar = inject(MatSnackBar);

  isEditMode = signal(false);
  private authorId: number | null = null;

  authorForm = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    nationality: ['', Validators.required]
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.authorId = +id;
      this.authorService.findById(+id).subscribe({
        next: (author) => this.authorForm.patchValue({
          firstName: author.firstName,
          lastName: author.lastName,
          nationality: author.nationality
        }),
        error: () => {}
      });
    }
  }

  saveAuthor(): void {
    if (this.authorForm.invalid) return;
    const formValue = this.authorForm.getRawValue();
    const request: AuthorRequest = {
      firstName: formValue.firstName ?? '',
      lastName: formValue.lastName ?? '',
      nationality: formValue.nationality ?? ''
    };
    if (this.isEditMode() && this.authorId) {
      this.authorService.update(this.authorId, request).subscribe({
        next: () => {
          this.snackBar.open('Autor actualizado correctamente', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/owner/authors']);
        },
        error: () => {}
      });
    } else {
      this.authorService.create(request).subscribe({
        next: () => {
          this.snackBar.open('Autor creado correctamente', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/owner/authors']);
        },
        error: () => {}
      });
    }
  }
}
