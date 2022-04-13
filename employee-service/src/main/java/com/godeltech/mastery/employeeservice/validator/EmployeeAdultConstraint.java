package com.godeltech.mastery.employeeservice.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = EmployeeAdultValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface EmployeeAdultConstraint {
    String message() default "{employee.validation.dateOfBirth.adultConstraint}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
