package com.godeltech.mastery.employeeservice.service.impl;

import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.service.KafkaMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Kafka.REPLY_TOPIC_EMPLOYEE_CREATION;
import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Kafka.TOPIC_EMPLOYEE_CREATION;


@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageSenderImpl implements KafkaMessageSender {

    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Override
    public void send(EmployeeDtoResponse employeeDtoResponse) {
        log.info("Send value by kafka employeeDtoResponse:{}", employeeDtoResponse);
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC_EMPLOYEE_CREATION, employeeDtoResponse);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, REPLY_TOPIC_EMPLOYEE_CREATION.getBytes(StandardCharsets.UTF_8)));
        RequestReplyFuture<String, Object, Object> sendAndReceive = replyingKafkaTemplate.sendAndReceive(record);
        sendAndReceive.addCallback(result -> log.info("Reply value was received:{}", result.value()),
                ex -> log.error("Reply value wasn't received:{}", ex.getMessage()));
    }
}
