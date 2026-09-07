import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API_BASE_URL } from '../api-config';

export interface TokenResponse {
  token: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<TokenResponse> {
    return this.http
      .post<TokenResponse>(`${API_BASE_URL}/auth/login`, { email, password })
      .pipe(
        tap((response) => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('role', response.role);
        })
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
