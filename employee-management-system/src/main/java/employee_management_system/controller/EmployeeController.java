
package employee_management_system.controller;

import employee_management_system.dto.EmployeeDTO;
import employee_management_system.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Add Employee
    @PostMapping
    public ResponseEntity<EmployeeDTO> addEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        EmployeeDTO savedEmployee =
                employeeService.addEmployee(employeeDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedEmployee);
    }

    // Get All Employees
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {

        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // Get Employee By ID
    @GetMapping("/{empNo}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(
            @PathVariable Long empNo) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(empNo));
    }

    // Update Employee
    @PutMapping("/{empNo}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long empNo,
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(empNo, employeeDTO));
    }

    // Delete Employee
    @DeleteMapping("/{empNo}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long empNo) {

        employeeService.deleteEmployee(empNo);

        return ResponseEntity.noContent().build();
    }

    // Employee Reports using Query Parameters
    @GetMapping("/reports")
    public ResponseEntity<List<EmployeeDTO>> getEmployeeReports(
            @RequestParam(required = false) String letter,
            @RequestParam(required = false) Integer years,
            @RequestParam(required = false) BigDecimal minSalary) {

        int parameterCount = 0;

        if (letter != null) parameterCount++;
        if (years != null) parameterCount++;
        if (minSalary != null) parameterCount++;

        if (parameterCount != 1) {
            throw new IllegalArgumentException(
                    "Provide exactly one report parameter: letter, years, or minSalary");
        }

        List<EmployeeDTO> employees;

        if (letter != null) {

            if (letter.isBlank()) {
                throw new IllegalArgumentException(
                        "Letter cannot be blank");
            }

            employees = employeeService.getEmployeesByPrefix(letter);

        } else if (years != null) {

            if (years < 0) {
                throw new IllegalArgumentException(
                        "Years cannot be negative");
            }

            employees = employeeService.getEmployeesByExperience(years);

        } else {

            if (minSalary.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Minimum salary must be greater than zero");
            }

            employees = employeeService.getEmployeesBySalary(minSalary);
        }

        return ResponseEntity.ok(employees);
    }
}
