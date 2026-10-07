package in.Garvit.Bilionn.io;

import in.Garvit.Bilionn.entity.AccountStatus;
import in.Garvit.Bilionn.entity.Address;
import in.Garvit.Bilionn.entity.BankDetails;
import in.Garvit.Bilionn.entity.EmergencyContact;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeUpdateRequest {

    private String fullName;

    private LocalDate dateOfBirth;

    private String mobileNumber;

    private Address address;

    private LocalDate joiningDate;

    private BigDecimal salary;

    private String panNumber;

    private String aadhaarNumber;

    private BankDetails bankDetails;

    private EmergencyContact emergencyContact;

    private String email;

    private AccountStatus accountStatus;

}
