import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.errorMessage = '';
    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {
        if (response.role === 'admin') {
          this.router.navigate(['/admin/conversations']);
        } else {
          this.router.navigate(['/chat']);
        }
      },
      error: () => {
        this.errorMessage = 'Email ou mot de passe incorrect';
      }
    });
  }
}
