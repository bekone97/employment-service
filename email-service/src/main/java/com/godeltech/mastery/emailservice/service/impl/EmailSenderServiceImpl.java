package com.godeltech.mastery.emailservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.godeltech.mastery.emailservice.dto.EmployeePayload;
import com.godeltech.mastery.emailservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {



    public void sendEmail(EmployeePayload employee) {
       log.info("Got employee= {}",employee);

    }

    @Override
    public void sendEmailAboutCreationEmployee(EmployeePayload employeePayload){
        sendEmail(employeePayload);

    }
}
