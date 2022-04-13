package com.godeltech.mastery.employeeservice.handling;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationMessage {

    private String field;

    private String message;
}
