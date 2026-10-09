import { Routes } from '@angular/router';
import { CreateBillMaker } from '../createBillMaker/signup';
import { adminGuard } from '../guards/admin.guard';
import { unsavedChangesGuard } from '../guards/unsaved-changes.guard';

export const adminRoutes: Routes = [
  {
    path: 'admin/users/new',
    component: CreateBillMaker,
    canActivate: [adminGuard],
    canDeactivate: [unsavedChangesGuard],
    title: 'Add cashier | Bilionn',
  },
];