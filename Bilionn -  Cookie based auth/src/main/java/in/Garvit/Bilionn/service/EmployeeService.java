package in.Garvit.Bilionn.service;

import in.Garvit.Bilionn.io.CashierRegistrationRequest;
import in.Garvit.Bilionn.io.EmployeeResponse;
import in.Garvit.Bilionn.io.EmployeeUpdateRequest;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse create(CashierRegistrationRequest request);
    List<EmployeeResponse> findAll();
    EmployeeResponse findByCode(String employeeCode);
    EmployeeResponse update(String employeeCode, EmployeeUpdateRequest request);
    void delete(String employeeCode);
}
