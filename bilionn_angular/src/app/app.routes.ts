import { Routes } from '@angular/router';
import { adminGuard } from './admin.guard';
import { CreateBillMaker } from './signup/signup';
import { Login } from './login/login';
import { Dashboard } from './dashboard/dashboard';

export const routes: Routes = [
  { path: 'login', component: Login, title: 'Sign in | Bilionn' },
  { path: 'dashboard', component: Dashboard, title: 'Dashboard | Bilionn' },
  { path: 'admin/users/new', component: CreateBillMaker, canActivate: [adminGuard], title: 'Add cashier | Bilionn' },
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  { path: '**', redirectTo: 'login' },
];
