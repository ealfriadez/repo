import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from '../../../core/services/user.service';

@Component({
  selector: 'app-customer-profile',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule, MatInputModule, MatButtonModule,
    MatSnackBarModule, MatCardModule, MatIconModule
  ],
  templateUrl: './customer-profile.component.html',
  styleUrl: './customer-profile.component.css'
})
export class CustomerProfileComponent implements OnInit {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private snackBar = inject(MatSnackBar);

  savingProfile = signal(false);
  savingPassword = signal(false);
  hideCurrentPw = signal(true);
  hideNewPw = signal(true);

  profileForm = this.fb.group({
    firstName: ['', [Validators.maxLength(100)]],
    lastName: ['', [Validators.maxLength(100)]],
    phone: ['', [Validators.maxLength(20)]]
  });

  passwordForm = this.fb.group({
    currentPassword: ['', [Validators.required]],
    newPassword: ['', [
      Validators.required,
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,}$/)
    ]]
  });

  ngOnInit(): void {
    this.userService.getProfile().subscribe({
      next: (data) => this.profileForm.patchValue({
        firstName: data.firstName,
        lastName: data.lastName,
        phone: data.phone
      }),
      error: () => {}
    });
  }

  updateProfile(): void {
    if (this.profileForm.invalid) return;
    this.savingProfile.set(true);
    this.userService.updateProfile(this.profileForm.value as any).subscribe({
      next: () => {
        this.savingProfile.set(false);
        this.snackBar.open('Datos actualizados', 'Cerrar', { duration: 3000 });
      },
      error: () => this.savingProfile.set(false)
    });
  }

  changePassword(): void {
    if (this.passwordForm.invalid) return;
    this.savingPassword.set(true);
    this.userService.changePassword(this.passwordForm.value as any).subscribe({
      next: () => {
        this.savingPassword.set(false);
        this.passwordForm.reset();
        this.snackBar.open('Contraseña actualizada', 'Cerrar', { duration: 3000 });
      },
      error: () => this.savingPassword.set(false)
    });
  }
}
