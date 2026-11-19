
package employee_management_system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

public class EmployeeDTO {

    private Long empno;

    @NotBlank(message = "Employee name cannot be blank")
    private String empName;

    @NotNull(message = "Date of joining is required")
    @PastOrPresent(message = "Date of joining cannot be in the future")
    private LocalDate doj;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    @NotNull(message = "Basic salary is required")
    @DecimalMin(value = "0.01", message = "Salary must be greater than zero")
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
