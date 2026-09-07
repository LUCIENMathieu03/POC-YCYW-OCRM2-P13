import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API_BASE_URL } from '../api-config';

export interface TokenResponse {
  token: string;
  role: string;
  id: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<TokenResponse> {
    return this.http
      .post<TokenResponse>(`${API_BASE_URL}/auth/login`, { email, password })
      .pipe(
        tap((response) => {
          sessionStorage.setItem('token', response.token);
          sessionStorage.setItem('role', response.role);
          sessionStorage.setItem('userId', String(response.id));
        })
      );
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  getRole(): string | null {
    return sessionStorage.getItem('role');
  }

  getUserId(): number | null {
    const value = sessionStorage.getItem('userId');
    return value ? Number(value) : null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
