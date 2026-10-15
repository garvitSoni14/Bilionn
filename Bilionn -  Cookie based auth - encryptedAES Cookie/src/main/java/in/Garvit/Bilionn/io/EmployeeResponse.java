package in.Garvit.Bilionn.io;

import in.Garvit.Bilionn.entity.AccountStatus;
import in.Garvit.Bilionn.entity.Address;
import in.Garvit.Bilionn.entity.BankDetails;
import in.Garvit.Bilionn.entity.EmergencyContact;
import in.Garvit.Bilionn.entity.Role;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;

    private String employeeCode;

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

    private Role role;

    private AccountStatus accountStatus;

    private String userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
