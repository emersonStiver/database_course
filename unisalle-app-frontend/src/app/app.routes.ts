import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginFormComponent } from './features/login-form/login-form.component';
import { UserProfileComponent } from './features/user-profile/user-profile.component';
import { authGuard, noAuthGuard } from './core/guards/user.guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'user-profile',
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: LoginFormComponent,
        canActivate:[noAuthGuard]
    },
    {
        path: 'user-profile',
        component: UserProfileComponent,
        canActivate: [authGuard]
    },
    {
        path: '**',
        redirectTo: '',
    }

];
