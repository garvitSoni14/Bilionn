import { Component, input } from '@angular/core';
import { FieldTree, FormField } from '@angular/forms/signals';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { EmploymentData } from '../models/employee.model';

@Component({
  selector: 'app-employment-section',
  imports: [FormField, MatFormField, MatLabel, MatError, MatInput],
  templateUrl: './employment-section.html',
})
export class EmploymentSection {
  readonly form = input.required<FieldTree<EmploymentData>>();
}