
package employee_management_system.repository;

import employee_management_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find employees whose names start with a letter
    List<Employee> findByEmpNameStartingWithIgnoreCase(String letter);

    // Find employees who joined before a particular date
    List<Employee> findByDojBefore(LocalDate cutoffDate);

    // Find employees whose salary is greater than or equal to a value
    List<Employee> findByBsalaryGreaterThanEqual(BigDecimal salary);

}
