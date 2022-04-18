package com.godeltech.mastery.emailservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.godeltech.mastery.emailservice.dto.EmployeePayload;
import com.godeltech.mastery.emailservice.utils.ConstantUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;


import javax.mail.MessagingException;

import static com.godeltech.mastery.emailservice.utils.ConstantUtil.Kafka.TOPIC_EMPLOYEE_CREATION;


public interface KafkaListenerService {

    @KafkaListener(groupId = TOPIC_EMPLOYEE_CREATION,topics = {ConstantUtil.Kafka.TOPIC_EMPLOYEE_CREATION},containerFactory = "listenerContainerFactory")
    @SendTo
    Message<EmployeePayload> employeePayloadCreation(Message<EmployeePayload> employeePayload) throws JsonProcessingException, MessagingException;

    @KafkaListener(groupId = TOPIC_EMPLOYEE_CREATION,topics = {ConstantUtil.Kafka.TOPIC_EMPLOYEE_CREATION+".DLT"},containerFactory = "listenerContainerFactory")
    @SendTo
    void unknown(ConsumerRecord<String,Object> object);
}
