package com.godeltech.mastery.emailservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.godeltech.mastery.emailservice.dto.EmployeePayload;


public interface EmailSenderService {
    void sendEmailAboutCreationEmployee(EmployeePayload employeePayload) throws JsonProcessingException;
}
