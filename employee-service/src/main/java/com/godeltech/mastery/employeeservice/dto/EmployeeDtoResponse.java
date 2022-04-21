package com.godeltech.mastery.employeeservice.dto;

import com.godeltech.mastery.employeeservice.dao.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDtoResponse {

    @Schema(description = "Employee id, which applied application",example = "1")
    private Long employeeId;

    @Schema(description = "Employee first name",example = "Artem")
    private String firstName;

    @Schema(description = "Employee last name",example = "Miachyn")
    private String lastName;

    @Schema(description = "Employee department",example = "2")
    private DepartmentDtoResponse department;

    @Schema(description = "Employee job tittle",example = "IT")
    private String jobTittle;

    @Schema(description = "Employee gender",example = "MALE")
    private Gender gender;

    @Schema(description = "Employee date of birth",example = "2000-04-05")
    private LocalDate dateOfBirth;

    @Schema(description = "Phone numbers of employee")
    private List<PhoneDto> phones;
}
