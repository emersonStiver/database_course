import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../core/services/user.service';
import { Router } from '@angular/router';
import { catchError, of } from 'rxjs';
import { User } from '../../core/models/users';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './login-form.component.html',
})
export class LoginFormComponent {
  showSignup = false;
  loginError: string | null = null;
  signupError: string | null = null;

  loginForm!: FormGroup;
  signupForm!: FormGroup;



  constructor(private fb: FormBuilder, private auth: UserService, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  
    this.signupForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
    
  }

  onLoginSubmit() {
    if (this.loginForm.invalid) return;

    const { email, password } = this.loginForm.value;
    this.auth.login(email!, password!).pipe(
      catchError(err => {
        this.loginError = err.error?.message || 'Login failed';
        return of(null);
      })
    ).subscribe(res => {
      if (res) this.router.navigate(['/user-profile']);
    });
  }

  onSignupSubmit() {
    if (this.signupForm.invalid) return;
  
    const { name, email, password } = this.signupForm.value;
    this.auth.signup(name!, email!, password!).pipe(
      catchError(err => {
        this.signupError = err.error?.message || 'Signup failed';
        return of(null);
      })
    ).subscribe(res => {
      if (res) this.router.navigate(['/user-profile']);
    });
  }
  
}