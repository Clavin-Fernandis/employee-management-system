
package employee_management_system.service;

import employee_management_system.dto.EmployeeDTO;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {

    // CRUD Operations
    EmployeeDTO addEmployee(EmployeeDTO employeeDTO);

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById(Long empNo);

    EmployeeDTO updateEmployee(Long empNo, EmployeeDTO employeeDTO);

    void deleteEmployee(Long empNo);

    // Report Operations
    List<EmployeeDTO> getEmployeesByPrefix(String letter);

    List<EmployeeDTO> getEmployeesByExperience(int years);

    List<EmployeeDTO> getEmployeesBySalary(BigDecimal minSalary);
}
