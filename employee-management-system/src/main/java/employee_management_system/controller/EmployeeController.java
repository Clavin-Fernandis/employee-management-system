
package employee_management_system.controller;

import employee_management_system.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import employee_management_system.dto.EmployeeDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> addEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        EmployeeDTO savedEmployee =
                employeeService.addEmployee(employeeDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedEmployee);
    }


    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {

        List<EmployeeDTO> employees =
                employeeService.getAllEmployees();

        return ResponseEntity.ok(employees);
    }


    @GetMapping("/{empNo}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(
            @PathVariable Long empNo) {

        EmployeeDTO employee =
                employeeService.getEmployeeById(empNo);

        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{empNo}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long empNo,
            @RequestBody EmployeeDTO employeeDTO) {

        EmployeeDTO updatedEmployee =
                employeeService.updateEmployee(empNo, employeeDTO);

        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{empNo}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long empNo) {

        employeeService.deleteEmployee(empNo);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reports/prefix/{letter}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPrefix(
            @PathVariable String letter) {

        List<EmployeeDTO> employees =
                employeeService.getEmployeesByPrefix(letter);

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/reports/experience/{years}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByExperience(
            @PathVariable int years) {

        List<EmployeeDTO> employees =
                employeeService.getEmployeesByExperience(years);

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/reports/salary/{minSalary}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesBySalary(
            @PathVariable BigDecimal minSalary) {

        List<EmployeeDTO> employees =
                employeeService.getEmployeesBySalary(minSalary);

        return ResponseEntity.ok(employees);
    }


}
