package in.Garvit.Bilionn.io;

import in.Garvit.Bilionn.entity.Address;
import in.Garvit.Bilionn.entity.BankDetails;
import in.Garvit.Bilionn.entity.EmergencyContact;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CashierRegistrationRequest {
    @NotBlank(message = "Employee code is required")
    @Size(max = 30, message = "Employee code must not exceed 30 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9-]+$",
            message = "Employee code may contain only letters, numbers and hyphens"
    )


    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String employeeCode;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10-digit mobile number"
    )
    private String mobileNumber;

    @NotNull(message = "Address is required")
    @Valid
    private Address address;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @NotNull(message = "Salary is required")
    @DecimalMin(
            value = "0.01",
            message = "Salary must be greater than 0"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Salary must have at most 10 integer digits and 2 decimal places"
    )
    private BigDecimal salary;

    @Size(max = 10, message = "PAN must be 10 characters")
    @Pattern(
            regexp = "^[A-Za-z]{5}\\d{4}[A-Za-z]$",
            message = "PAN must be in the format ABCDE1234F"
    )
    private String panNumber;

    @Size(max = 12, message = "Aadhaar must be 12 digits")
    @Pattern(
            regexp = "^\\d{12}$",
            message = "Aadhaar must be 12 digits"
    )
    private String aadhaarNumber;

    @NotNull(message = "Bank details are required")
    @Valid
    private BankDetails bankDetails;

    @NotNull(message = "Emergency contact is required")
    @Valid
    private EmergencyContact emergencyContact;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 120, message = "Email must not exceed 120 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "Password must contain uppercase, lowercase, number and special character"
    )
    private String password;
}
