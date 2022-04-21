package com.godeltech.mastery.emailservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.godeltech.mastery.emailservice.dto.EmployeePayload;
import com.godeltech.mastery.emailservice.service.EmailSenderService;
import com.godeltech.mastery.emailservice.service.EmployeeCreationListenerService;
import com.godeltech.mastery.emailservice.utils.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import static com.godeltech.mastery.emailservice.utils.ConstantUtil.Kafka.TOPIC_EMPLOYEE_CREATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEmployeeCreationListenerServiceImpl implements EmployeeCreationListenerService {

    private final EmailSenderService emailSenderService;


    @Override
    @KafkaListener(groupId = TOPIC_EMPLOYEE_CREATION,topics = {ConstantUtil.Kafka.TOPIC_EMPLOYEE_CREATION},containerFactory = "listenerContainerFactory")
    @SendTo
    public EmployeePayload employeePayloadCreation(EmployeePayload employeePayload) throws JsonProcessingException, MessagingException {
        log.info("Received employeePayload :{}", employeePayload);
        emailSenderService.sendEmailAboutCreationEmployee(employeePayload);
        return employeePayload;
    }


    @Override
    @KafkaListener(groupId = TOPIC_EMPLOYEE_CREATION,topics = {ConstantUtil.Kafka.TOPIC_EMPLOYEE_CREATION+".DLT"},containerFactory = "listenerContainerFactory")
    @SendTo
    public void unknown(ConsumerRecord<String, Object> object,
                        @Header String header) {
        log.error("Received unknown object with value :{} and header:{}", object.value(),header);
    }
}
