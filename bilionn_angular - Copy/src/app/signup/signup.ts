import { Component, inject, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterLink } from '@angular/router';

interface CreatedUser {
  name: string;
  email: string;
  role: string;
}

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './signup.html',
  styleUrl: './signup.css',
})
export class CreateBillMaker {
  private readonly http = inject(HttpClient);
  readonly loading = signal(false);
  readonly successMessage = signal('');
  readonly errorMessage = signal('');
  readonly adminLoginRequired = signal(false);
  readonly formModel = {
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
  };

  submit(form: NgForm): void {
    this.successMessage.set('');
    this.errorMessage.set('');
    this.adminLoginRequired.set(false);
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    const { name, email, password, confirmPassword } = this.formModel;
    if (password !== confirmPassword) {
      this.errorMessage.set('Your passwords do not match.');
      return;
    }

    this.loading.set(true);
    this.http.post<CreatedUser>('http://localhost:8080/api/v1.0/admin/register', {
      name,
      email,
      password,
      role: 'ROLE_USER',
    }, { withCredentials: true }).subscribe({
      next: () => {
        this.successMessage.set('Bill maker account created successfully.');
        form.resetForm();
        this.loading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        const serverMessage = error.error?.message ?? error.error?.detail;
        if (error.status === 401 || error.status === 403) {
          this.adminLoginRequired.set(true);
          this.errorMessage.set('This request requires an administrator session in this browser.');
        } else {
          this.errorMessage.set(serverMessage || (error.status === 0
            ? 'Could not reach the server. Check that the backend is running and try again.'
            : 'We could not create the account. Please check the entered details and try again.'));
        }
        this.loading.set(false);
      },
    });
  }
}
