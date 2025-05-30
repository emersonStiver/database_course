import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../core/services/user.service';
import { User } from '../../core/models/users';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [NgIf],
  templateUrl: './user-profile.component.html',
})
export class UserProfileComponent implements OnInit {
  userData: User | null = null;
  error: string | null = null;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {
    const token = localStorage.getItem('auth_token');
    if (!token) {
      this.router.navigate(['/login']);
    }
  }

  fetchUserDetails() {
    console.log('Fetching user details...', this.userService.getUser());
    this.userData = this.userService.getUser();
  }

  logout() {
    this.userService.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
  }
  
}
