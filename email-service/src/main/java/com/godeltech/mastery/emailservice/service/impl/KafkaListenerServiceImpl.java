package com.godeltech.mastery.emailservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.godeltech.mastery.emailservice.dto.EmployeePayload;
import com.godeltech.mastery.emailservice.service.EmailSenderService;
import com.godeltech.mastery.emailservice.service.KafkaListenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import static com.godeltech.mastery.emailservice.utils.ConstantUtil.Kafka.REPLY_TOPIC_EMPLOYEE_CREATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaListenerServiceImpl implements KafkaListenerService {

    private final EmailSenderService emailSenderService;


    @Override
    public Message<EmployeePayload> employeePayloadCreation(Message<EmployeePayload> employeePayload) throws JsonProcessingException, MessagingException {
        log.info("Received employeePayload :{}", employeePayload.getPayload());
        emailSenderService.sendEmailAboutCreationEmployee(employeePayload.getPayload());
        return MessageBuilder.
                fromMessage(employeePayload)
                .setHeader(KafkaHeaders.TOPIC, REPLY_TOPIC_EMPLOYEE_CREATION)
                .build();
    }

    @Override
    public void unknown(ConsumerRecord<String, Object> object) {
        log.error("Received unknown object with value :{}", object.value());
    }
}
