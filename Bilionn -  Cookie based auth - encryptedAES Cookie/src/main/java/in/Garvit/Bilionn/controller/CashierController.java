package in.Garvit.Bilionn.controller;

import in.Garvit.Bilionn.io.CashierRegistrationRequest;
import in.Garvit.Bilionn.io.EmployeeResponse;
import in.Garvit.Bilionn.io.EmployeeUpdateRequest;
import in.Garvit.Bilionn.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/cashiers")
@RequiredArgsConstructor
public class CashierController {
    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse create(@Valid @RequestBody CashierRegistrationRequest request ) {
        try {
            return employeeService.create(request);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public List<EmployeeResponse> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{employeeCode}")
    public EmployeeResponse findByCode(@PathVariable String employeeCode) {
        try {
            return employeeService.findByCode(employeeCode);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{employeeCode}")
    public EmployeeResponse update(@PathVariable String employeeCode, @RequestBody EmployeeUpdateRequest request) {
        try {
            return employeeService.update(employeeCode, request);
        } catch (IllegalArgumentException e) {
            HttpStatus status = e.getMessage() != null && e.getMessage().startsWith("Employee not found")
                    ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            throw new ResponseStatusException(status, e.getMessage());
        }
    }

    @DeleteMapping("/{employeeCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String employeeCode) {
        try {
            employeeService.delete(employeeCode);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
