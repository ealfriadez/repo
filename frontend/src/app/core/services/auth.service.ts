import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { ROLES } from '../constants/roles';
import { LoginRequest, RegisterRequest, AuthResponse } from '../../models/auth.model';
import { environment } from '../../../environments/environment';
import { StorageService } from './storage.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = environment.apiUrl + '/api/auth';

  private _isLoggedIn = signal(false);
  private _user = signal<AuthResponse | null>(null);

  readonly isLoggedIn = this._isLoggedIn.asReadonly();
  readonly user = this._user.asReadonly();
  readonly role = computed(() => this._user()?.role ?? '');
  readonly email = computed(() => this._user()?.email ?? '');
  readonly isOwner = computed(() => this.role() === ROLES.OWNER);
  readonly isCustomer = computed(() => this.role() === ROLES.CUSTOMER);

  constructor(
    private http: HttpClient,
    private router: Router,
    private storage: StorageService
  ) {
    const user = this.storage.getUser();
    if (user) {
      this._user.set(user);
      this._isLoggedIn.set(true);
    }
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, request).pipe(
      tap(res => this.setSession(res))
    );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, request).pipe(
      tap(res => this.setSession(res))
    );
  }

  clearSession(): void {
    this.storage.clearSession();
    this._isLoggedIn.set(false);
    this._user.set(null);
  }

  logout(): void {
    localStorage.removeItem(`chat_messages_${this.email()}`);
    this.clearSession();
    this.router.navigate(['/login']);
  }

  getHomeRoute(): string {
    if (this.isOwner()) return '/owner/dashboard';
    return '/catalog';
  }

  getToken(): string | null {
    return this.storage.getToken();
  }

  private setSession(auth: AuthResponse): void {
    this.storage.saveSession(auth);
    this._isLoggedIn.set(true);
    this._user.set(auth);
  }
}
