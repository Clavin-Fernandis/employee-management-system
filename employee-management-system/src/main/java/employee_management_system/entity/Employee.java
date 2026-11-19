
package employee_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empno;

    @NotBlank(message = "Employee name cannot be blank")
    @Column(name = "EmpName", nullable = false, length = 100)
    private String empName;

    @NotNull(message = "Date of joining is required")
    @PastOrPresent(message = "Date of joining cannot be in the future")
    @Column(name = "DoJ", nullable = false)
    private LocalDate doj;

    @NotBlank(message = "Gender cannot be blank")
    @Column(name = "Gender", nullable = false, length = 10)
    private String gender;

    @NotNull(message = "Basic salary is required")
    @DecimalMin(value = "0.01", message = "Salary must be greater than zero")
    @Column(name = "Bsalary", nullable = false, precision = 10, scale = 2)
    private BigDecimal bsalary;

    // Getters and Setters

    public Long getEmpno() {
        return empno;
    }

    public void setEmpno(Long empno) {
        this.empno = empno;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public LocalDate getDoj() {
        return doj;
    }

    public void setDoj(LocalDate doj) {
        this.doj = doj;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getBsalary() {
        return bsalary;
    }

    public void setBsalary(BigDecimal bsalary) {
        this.bsalary = bsalary;
    }
}
