package com.godeltech.mastery.emailservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.godeltech.mastery.emailservice.dto.EmployeePayload;
import com.godeltech.mastery.emailservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;


    public void sendEmail(EmployeePayload employee) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String subject = "Creation of new employee";
        String content = "<h3>There is a new employee on the employement service:<h3><br>"
                + "Employee id - " + employee.getEmployeeId()
                + "<br> First name - " + employee.getFirstName()
                + "<br> Last name - " + employee.getLastName()
                + "<br> Department - " + employee.getDepartment().getDepartmentName()
                + "<br> Job tittle - " + employee.getJobTittle()
                + "<br> Gender - " + employee.getGender()
                + "<br> Date of birth - " + employee.getDateOfBirth()
                + "<h4>From email-service<h4>";
        helper.setFrom("employmentservicemastery@gmail.com");
        helper.setTo("myachinenergo@mail.ru");

        helper.setText(content, true);
        helper.setSubject(subject);

        javaMailSender.send(message);

    }

    @Override
    public void sendEmailAboutCreationEmployee(EmployeePayload employeePayload) throws JsonProcessingException, MessagingException {
        sendEmail(employeePayload);

    }
}
