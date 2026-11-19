
package employee_management_system.service;

import employee_management_system.dto.EmployeeDTO;
import employee_management_system.entity.Employee;
import employee_management_system.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    private void validateEmployeeDTO(EmployeeDTO dto) {

        // Validate employee name
        if (dto.getEmpName() == null ||
                dto.getEmpName().isBlank()) {
            throw new IllegalArgumentException(
                    "Employee name cannot be blank");
        }

        // Validate date of joining
        if (dto.getDoj() == null ||
                dto.getDoj().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date of joining cannot be in the future");
        }

        // Validate basic salary
        if (dto.getBsalary() == null ||
                dto.getBsalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Salary must be greater than zero");
        }
    }


    private EmployeeDTO convertToDTO(Employee employee) {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setEmpno(employee.getEmpno());
        dto.setEmpName(employee.getEmpName());
        dto.setDoj(employee.getDoj());
        dto.setGender(employee.getGender());
        dto.setBsalary(employee.getBsalary());

        return dto;
    }

    private Employee convertToEntity(EmployeeDTO dto) {

        Employee employee = new Employee();

        employee.setEmpName(dto.getEmpName());
        employee.setDoj(dto.getDoj());
        employee.setGender(dto.getGender());
        employee.setBsalary(dto.getBsalary());

        return employee;
    }

    @Override
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        validateEmployeeDTO(employeeDTO);
        // Convert DTO into Entity
        Employee employee = convertToEntity(employeeDTO);

        // Save employee in MySQL
        Employee savedEmployee = employeeRepository.save(employee);

        // Convert saved Entity into DTO
        return convertToDTO(savedEmployee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {

        // Fetch all employees from database
        List<Employee> employees = employeeRepository.findAll();

        // Convert Entity list into DTO list
        return employees.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public EmployeeDTO getEmployeeById(Long empNo) {

        // Search employee by ID
        Employee employee = employeeRepository.findById(empNo)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with ID: " + empNo));

        // Convert Entity into DTO
        return convertToDTO(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long empNo, EmployeeDTO employeeDTO) {
        validateEmployeeDTO(employeeDTO);
        // Check whether employee exists
        Employee employee = employeeRepository.findById(empNo)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with ID: " + empNo));

        // Update employee details
        employee.setEmpName(employeeDTO.getEmpName());
        employee.setDoj(employeeDTO.getDoj());
        employee.setGender(employeeDTO.getGender());
        employee.setBsalary(employeeDTO.getBsalary());

        // Save updated employee
        Employee updatedEmployee = employeeRepository.save(employee);

        // Return updated details
        return convertToDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long empNo) {

        // Check whether employee exists
        Employee employee = employeeRepository.findById(empNo)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with ID: " + empNo));

        // Delete employee
        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByPrefix(String letter) {

        // Search employees by starting letter
        List<Employee> employees =
                employeeRepository.findByEmpNameStartingWithIgnoreCase(letter);

        // Convert Entity list into DTO list
        return employees.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<EmployeeDTO> getEmployeesByExperience(int years) {

        // Calculate the cutoff date
        LocalDate cutoffDate = LocalDate.now().minusYears(years);

        // Find employees who joined before the cutoff date
        List<Employee> employees =
                employeeRepository.findByDojBefore(cutoffDate);

        // Convert Entity list into DTO list
        return employees.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<EmployeeDTO> getEmployeesBySalary(BigDecimal minSalary) {

        // Find employees with salary greater than or equal to minimum salary
        List<Employee> employees =
                employeeRepository.findByBsalaryGreaterThanEqual(minSalary);

        // Convert Entity list into DTO list
        return employees.stream()
                .map(this::convertToDTO)
                .toList();
    }


}
