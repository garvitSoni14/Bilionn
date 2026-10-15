package in.Garvit.Bilionn.entity;

import jakarta.persistence.Embeddable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankDetails {

    @NotBlank(message = "Account holder name is required")
    @Size(max = 100, message = "Account holder name must not exceed 100 characters")
    private String accountHolderName;

    @NotBlank(message = "Account number is required")
    @Pattern(
            regexp = "^\\d{9,18}$",
            message = "Account number must contain 9 to 18 digits"
    )
    private String accountNumber;

    @NotBlank(message = "IFSC code is required")
    @Size(max = 11, message = "IFSC code must be 11 characters")
    @Pattern(
            regexp = "^[A-Za-z]{4}0[A-Za-z0-9]{6}$",
            message = "Enter a valid IFSC code"
    )
    private String ifscCode;

    @NotBlank(message = "Bank name is required")
    @Size(max = 100, message = "Bank name must not exceed 100 characters")
    private String bankName;
}
