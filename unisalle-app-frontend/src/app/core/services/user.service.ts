import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { AuthResponse, User } from '../models/users'; 

@Injectable({
  providedIn: 'root'
})
export class  UserService {

  private baseUrl = '/api/v1/users';
  private tokenKey = 'auth_token';
  private userSubject = new BehaviorSubject<any>(null);
  user$ = this.userSubject.asObservable(); //expose read-only observable, can't call next()

  constructor(private http: HttpClient, private router: Router) { 
    const token = this.getToken();
  }

  logout(): Observable<boolean> {
    return this.http.post<boolean>(`${this.baseUrl}/logout`, {}).pipe(
      tap(() => {
        localStorage.removeItem(this.tokenKey);
        this.userSubject.next(null);
      }),
      catchError(error => {
        console.error('Logout error:', error);
        return throwError(() => error);
      })
    );
  }
  

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`/api/login`, { email: email, password: password }).pipe(
      tap(response => {
        localStorage.setItem(this.tokenKey, response.accessToken);
        this.fetchUser().subscribe();
      }),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(()=> error); 
      })
    );
  }

  signup(name: string, email: string, password: string): Observable<any> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/signup`, { email, password, name }).pipe(
      tap(response => {
        localStorage.setItem(this.tokenKey, response.accessToken);
        this.fetchUser().subscribe();
      }),
      catchError(error => {
        console.error('Signup error:', error);
        return throwError(()=> error); 
      })
    );
  }


  fetchUser(): Observable<any>{
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.getToken()}`
    })
    return this.http.get<any>(`${this.baseUrl}/me`, { headers }).pipe(
      tap(user => this.userSubject.next(user))
    )
  }

  getUser(): User | null {
    return this.userSubject.value; 
  }

  getToken(): String | null {
    return localStorage.getItem(this.tokenKey);
  }
  isLoggedIn(): boolean {
    return this.getToken() !== null && this.getToken() !== undefined; 
  }
}
