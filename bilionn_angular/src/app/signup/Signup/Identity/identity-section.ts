import { Component, input } from '@angular/core';
import { FieldTree, FormField } from '@angular/forms/signals';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { IdentityData } from '../models/identity.model';

@Component({
  selector: 'app-identity-section',
  imports: [FormField, MatFormField, MatLabel, MatError, MatInput],
  templateUrl: './identity-section.html',
})
export class IdentitySection {
  readonly form = input.required<FieldTree<IdentityData>>();
  
}