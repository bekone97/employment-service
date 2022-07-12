package com.godeltech.mastery.emailservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.godeltech.mastery.emailservice.dto.EmployeePayload;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.messaging.handler.annotation.Header;



public interface EmployeeCreationListenerService {


    EmployeePayload employeePayloadCreation(EmployeePayload employeePayload) throws JsonProcessingException;


    void unknown(ConsumerRecord<String, Object> object,
                 @Header String header);
}
