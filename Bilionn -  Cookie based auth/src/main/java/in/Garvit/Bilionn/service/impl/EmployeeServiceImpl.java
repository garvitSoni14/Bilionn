package in.Garvit.Bilionn.service.impl;

import in.Garvit.Bilionn.entity.*;
import in.Garvit.Bilionn.io.CashierRegistrationRequest;
import in.Garvit.Bilionn.io.EmployeeResponse;
import in.Garvit.Bilionn.io.EmployeeUpdateRequest;
import in.Garvit.Bilionn.repository.EmployeeRepository;
import in.Garvit.Bilionn.repository.UserRepository;
import in.Garvit.Bilionn.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResponse create(CashierRegistrationRequest request) {
        require(request.getEmail(), "Email is required");
        require(request.getPassword(), "Password is required");
        require(request.getFullName(), "Full name is required");
        if (userRepository.existsByEmail(request.getEmail())) throw new IllegalArgumentException("Email is already in use");
        String code = request.getEmployeeCode() == null || request.getEmployeeCode().isBlank()
                ? "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
                : request.getEmployeeCode().trim();
        if (employeeRepository.existsByEmployeeCode(code)) throw new IllegalArgumentException("Employee code is already in use");
        checkUniqueIds(request.getPanNumber(), request.getAadhaarNumber(), null);

        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity admin = userRepository.findByEmail(adminEmail).orElseThrow(() -> new IllegalStateException("Signed-in administrator was not found"));
        UserEntity account = UserEntity.builder()
                .userId(UUID.randomUUID().toString()).email(request.getEmail().trim())
                .password(passwordEncoder.encode(request.getPassword())).name(request.getFullName().trim())
                .role(Role.CASHIER).accountStatus(AccountStatus.ACTIVE).build();
        account = userRepository.save(account);
        EmployeeEntity employee = EmployeeEntity.builder()
                .employeeCode(code).fullName(request.getFullName().trim()).dateOfBirth(request.getDateOfBirth())
                .mobileNumber(request.getMobileNumber()).address(request.getAddress()).joiningDate(request.getJoiningDate())
                .salary(request.getSalary()).panNumber(blankToNull(request.getPanNumber()))
                .aadhaarNumber(blankToNull(request.getAadhaarNumber())).bankDetails(request.getBankDetails())
                .emergencyContact(request.getEmergencyContact()).userAccount(account).createdByAdmin(admin).build();
        return toResponse(employeeRepository.save(employee));
    }

    @Override @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() { return employeeRepository.findAll().stream().map(this::toResponse).toList(); }

    @Override @Transactional(readOnly = true)
    public EmployeeResponse findByCode(String code) { return toResponse(find(code)); }

    @Override
    public EmployeeResponse update(String code, EmployeeUpdateRequest request) {
        EmployeeEntity employee = find(code);
        if (request.getFullName() != null) {
            employee.setFullName(request.getFullName().trim());
            employee.getUserAccount().setName(request.getFullName().trim());
        }
        if (request.getDateOfBirth() != null) employee.setDateOfBirth(request.getDateOfBirth());
        if (request.getMobileNumber() != null) employee.setMobileNumber(request.getMobileNumber());
        if (request.getAddress() != null) employee.setAddress(request.getAddress());
        if (request.getJoiningDate() != null) employee.setJoiningDate(request.getJoiningDate());
        if (request.getSalary() != null) employee.setSalary(request.getSalary());
        if (request.getBankDetails() != null) employee.setBankDetails(request.getBankDetails());
        if (request.getEmergencyContact() != null) employee.setEmergencyContact(request.getEmergencyContact());
        UserEntity account = employee.getUserAccount();
        if (request.getEmail() != null && !request.getEmail().isBlank()
                && !request.getEmail().equalsIgnoreCase(account.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail().trim())) throw new IllegalArgumentException("Email is already in use");
            account.setEmail(request.getEmail().trim());
        }
        if (request.getAccountStatus() != null) account.setAccountStatus(request.getAccountStatus());
        if (request.getPanNumber() != null) { checkUniqueIds(request.getPanNumber(), null, employee); employee.setPanNumber(blankToNull(request.getPanNumber())); }
        if (request.getAadhaarNumber() != null) { checkUniqueIds(null, request.getAadhaarNumber(), employee); employee.setAadhaarNumber(blankToNull(request.getAadhaarNumber())); }
        return toResponse(employeeRepository.save(employee));
    }

    @Override
    public void delete(String code) {
        EmployeeEntity employee = find(code);
        UserEntity account = employee.getUserAccount();
        employeeRepository.delete(employee);
        account.setAccountStatus(AccountStatus.DISABLED);
    }

    private EmployeeEntity find(String code) {
        return employeeRepository.findByEmployeeCode(code).orElseThrow(() -> new IllegalArgumentException("Employee not found: " + code));
    }
    private void checkUniqueIds(String pan, String aadhaar, EmployeeEntity current) {
        if (pan != null && !pan.isBlank() && employeeRepository.existsByPanNumber(pan)
                && (current == null || !pan.equals(current.getPanNumber()))) throw new IllegalArgumentException("PAN number is already in use");
        if (aadhaar != null && !aadhaar.isBlank() && employeeRepository.existsByAadhaarNumber(aadhaar)
                && (current == null || !aadhaar.equals(current.getAadhaarNumber()))) throw new IllegalArgumentException("Aadhaar number is already in use");
    }
    private void require(String value, String message) { if (value == null || value.isBlank()) throw new IllegalArgumentException(message); }
    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
    private EmployeeResponse toResponse(EmployeeEntity e) {
        UserEntity account = e.getUserAccount();
        return EmployeeResponse.builder().id(e.getId()).employeeCode(e.getEmployeeCode()).fullName(e.getFullName())
                .dateOfBirth(e.getDateOfBirth()).mobileNumber(e.getMobileNumber()).address(e.getAddress())
                .joiningDate(e.getJoiningDate()).salary(e.getSalary()).panNumber(e.getPanNumber())
                .aadhaarNumber(e.getAadhaarNumber()).bankDetails(e.getBankDetails()).emergencyContact(e.getEmergencyContact())
                .email(account.getEmail()).role(account.getRole()).accountStatus(account.getAccountStatus()).userId(account.getUserId())
                .createdAt(e.getCreatedAt()).updatedAt(e.getUpdatedAt()).build();
    }
}
