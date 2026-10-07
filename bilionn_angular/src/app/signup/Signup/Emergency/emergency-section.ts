import { Component, input } from '@angular/core';
import { FieldTree, FormField } from '@angular/forms/signals';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { EmergencyData } from '../models/emergency.model';

@Component({
  selector: 'app-emergency-section',
  imports: [FormField, MatFormField, MatLabel, MatError, MatInput],
  templateUrl: './emergency-section.html',
})
export class EmergencySection {
  readonly form = input.required<FieldTree<EmergencyData>>();
}