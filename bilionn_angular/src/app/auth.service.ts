import { Injectable,inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, firstValueFrom } from 'rxjs';

export interface LoginResponse {
  email: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly apiUrl = 'http://localhost:8080/api/v1.0';
  private readonly userStorageKey = 'bilionn.user';
  readonly currentUser = signal<LoginResponse | null>(this.restoreUser());

  constructor(private readonly http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials, { withCredentials: true }).pipe(
      tap((user) => {
        this.currentUser.set(user);
        sessionStorage.setItem(this.userStorageKey, JSON.stringify(user));
      }),
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/logout`, {}, { withCredentials: true }).pipe(
      tap(() => {
        this.currentUser.set(null);
        sessionStorage.removeItem(this.userStorageKey);
      }),
    );
  }

  private restoreUser(): LoginResponse | null {
    try {
      const storedUser = sessionStorage.getItem(this.userStorageKey);
      return storedUser ? JSON.parse(storedUser) as LoginResponse : null;
    } catch {
      return null;
    }
  }

  // This is the restoreSession method from session strg 
  async restoreSession(): Promise<void> {
  try {
    const user = await firstValueFrom(
      this.http.get<LoginResponse>(`${this.apiUrl}/me`, { withCredentials: true }),
    );
    this.currentUser.set(user);
    sessionStorage.setItem(this.userStorageKey, JSON.stringify(user));
  } catch {
    this.currentUser.set(null);
    sessionStorage.removeItem(this.userStorageKey);
  }
}
}
