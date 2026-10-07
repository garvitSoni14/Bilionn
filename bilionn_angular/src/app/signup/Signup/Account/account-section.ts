import { Component, input } from '@angular/core';
import { FieldTree, FormField } from '@angular/forms/signals';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { AccountData } from '../models/account.model';

@Component({
  selector: 'app-account-section',
  imports: [FormField, MatFormField, MatLabel, MatError, MatInput],
  templateUrl: './account-section.html',
})
export class AccountSection {
  readonly form = input.required<FieldTree<AccountData>>();
}