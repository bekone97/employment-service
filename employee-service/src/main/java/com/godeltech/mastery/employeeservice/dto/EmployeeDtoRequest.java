package com.godeltech.mastery.employeeservice.dto;

import com.godeltech.mastery.employeeservice.dao.entity.Gender;
import com.godeltech.mastery.employeeservice.dao.entity.Phone;
import com.godeltech.mastery.employeeservice.validator.EmployeeAdultConstraint;
import com.godeltech.mastery.employeeservice.validator.ValidId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDtoRequest {

    @Schema(description = "Employee first name",example = "Artem")
    @Size(min = 4, message = "{employee.validation.firstName.size.min}")
    @Size(max = 20, message = "{employee.validation.firstName.size.max}")
    @NotBlank(message = "{employee.validation.firstName.notBlank}")
    private String firstName;

    @Schema(description = "Employee last name",example = "Miachyn")
    @Size(min = 4, message = "{employee.validation.lastName.size.max}")
    @Size(max = 20, message = "{employee.validation.lastName.size.min}")
    @NotBlank(message = "{employee.validation.lastName.notBlank}")
    private String lastName;

    @Schema(description = "Employee department",example = "1")
    @ValidId
    private Long departmentId;

    @Schema(description = "Employee job tittle",example = "IT")
    @Size(min = 2, message = "{employee.validation.jobTittle.size.min}")
    @Size(max = 10, message = "{employee.validation.jobTittle.size.max}")
    @NotBlank(message = "{employee.validation.jobTittle.notBlank}")
    private String jobTittle;

    @Schema(description = "Employee gender",example = "MALE")
    @NotNull(message = "{employee.validation.gender.notNull}")
    private Gender gender;

    @Schema(description = "Employee first name",example = "2000-04-05")
    @EmployeeAdultConstraint
    @NotNull(message = "{employee.validation.dateOfBirth.notNull}")
    private LocalDate dateOfBirth;

    @Schema(description = "Phone numbers of employee")
    private List<PhoneDto> phones;
}
