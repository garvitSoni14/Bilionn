import { ChangeDetectionStrategy, Component, signal, inject } from '@angular/core';
import { form, submit } from '@angular/forms/signals';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { PersonalSection } from './Signup/sections/Personal/personal-section';
import { AddressSection } from './Signup/sections/Address/address-section';
import { EmploymentSection } from './Signup/sections/Employee/employment-section';
import { IdentitySection } from './Signup/sections/Identity/identity-section';
import { BankSection } from './Signup/sections/Bank/bank-section';
import { EmergencySection } from './Signup/sections/Emergency/emergency-section';
import { AccountSection } from './Signup/sections/Account/account-section';
import { createSignupFormData, SignupFormData, validateSignup } from './signup.model';
import { ValidationMessage } from './Signup/shared/validation/validation-message';

@Component({
  selector: 'app-signup',
  imports: [
    PersonalSection,
    AddressSection,
    EmploymentSection,
    IdentitySection,
    BankSection,
    EmergencySection,
    AccountSection,
    RouterLink,
    ValidationMessage    //Common validation message component

  ],
  templateUrl: './signup.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './signup.css',
})
export class CreateBillMaker {
  readonly loading = signal(false);
  readonly successMessage = signal('');
  readonly errorMessage = signal('');
  readonly adminLoginRequired = signal(false);
  private readonly http = inject(HttpClient);

  readonly model = signal<SignupFormData>(createSignupFormData());
  readonly signupForm = form(this.model, (path) => validateSignup(path));

// This signal will set the formSubmitted as true after successfull registration..
  // readonly formSubmittedSuccessfully = signal(false);

  async onSubmit(event: SubmitEvent): Promise<void> {
    event.preventDefault();

    this.successMessage.set('');
    this.errorMessage.set('');
    this.adminLoginRequired.set(false);

    await submit(this.signupForm, async () => {
      const formData = this.model();

      const payload = {
        employeeCode: formData.personal.employeeCode,
        fullName: formData.personal.fullName,
        dateOfBirth: formData.personal.dateOfBirth,
        mobileNumber: formData.personal.mobileNumber,
        address: formData.address,
        joiningDate: formData.employment.joiningDate,
        salary: Number(formData.employment.salary),
        panNumber: formData.identity.panNumber || null,
        aadhaarNumber: formData.identity.aadhaarNumber || null,
        bankDetails: formData.bank,
        emergencyContact: formData.emergency,
        email: formData.account.email,
        password: formData.account.password,
      };

      this.loading.set(true);

      try {
        await firstValueFrom(
          this.http.post(
            'http://localhost:8080/api/v1.0/admin/cashiers',
            payload,
          { withCredentials: true },
        )
      );

      // SUCCESS: on success show this message...
        this.successMessage.set('Cashier account created successfully.');        
        
        // (reset model to initial state without reseting the touched so validaiton were showing red error on whole form)
        // this.model.set(createSignupFormData());   

        // this.formSubmittedSuccessfully.set(true);

        this.signupForm().reset(createSignupFormData());

        console.log('Cashier account created successfully.', payload);

      } catch (error) {
        const httpError = error as HttpErrorResponse;
        if (httpError.status === 401 || httpError.status === 403) {
          this.adminLoginRequired.set(true);

          this.errorMessage.set('Sign in as an administrator in this browser, then try again.');
        } else {
          const message = httpError.error?.message ?? httpError.error?.detail;
          this.errorMessage.set(message || (httpError.status === 0
            ? 'Could not reach the server. Check that the backend is running and try again.'
            : 'We could not create the cashier. Check the details and try again.'));
        }
      } finally {
        this.loading.set(false);
      }
      return null;
    });
  }
}


