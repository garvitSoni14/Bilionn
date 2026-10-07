package in.Garvit.Bilionn.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "tbl_employees",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_employee_code", columnNames = "employeeCode"),
                @UniqueConstraint(name = "uk_pan_number", columnNames = "panNumber"),
                @UniqueConstraint(name = "uk_aadhaar_number", columnNames = "aadhaarNumber")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, length = 30)
    private String employeeCode;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 15)
    private String mobileNumber;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(length = 10)
    private String panNumber;

    @Column(length = 12)
    private String aadhaarNumber;

    @Embedded
    private BankDetails bankDetails;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "emergency_contact_name", length = 100)),
            @AttributeOverride(name = "mobileNumber", column = @Column(name = "emergency_contact_mobile_number", length = 15)),
            @AttributeOverride(name = "relationship", column = @Column(name = "emergency_contact_relationship", length = 50))
    })
    private EmergencyContact emergencyContact;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id", nullable = false, unique = true)
    private UserEntity userAccount;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 20)
//    private EmploymentStatus employmentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_admin_id", nullable = false)
    private UserEntity createdByAdmin;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
