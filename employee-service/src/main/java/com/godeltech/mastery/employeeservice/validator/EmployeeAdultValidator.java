package com.godeltech.mastery.employeeservice.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class EmployeeAdultValidator implements ConstraintValidator<EmployeeAdultConstraint, LocalDate> {

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(dateOfBirth)
                .map(dob -> Period.between(dob, LocalDate.now()).getYears())
                .map(age -> age >= 18)
                .orElse(true);
    }
}
