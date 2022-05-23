package com.godeltech.mastery.employeeservice.config;


import lombok.var;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Kafka.REPLY_TOPIC_EMPLOYEE_CREATION;


@Configuration
@ConditionalOnMissingBean({ConcurrentKafkaListenerContainerFactory.class})
public class KafkaConfig {

    @Value("${kafka.bootstrap.server}")
    private String kafkaServer;

    @Value("${kafka.groupId.config}")
    private String kafkaGroupIdConfig;

    @Bean
    public ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate(
            ProducerFactory<String, Object> pf,
            ConcurrentMessageListenerContainer<String, Object> lc) {
        var replyingKafkaTemplate = new ReplyingKafkaTemplate<>(pf, lc);
        replyingKafkaTemplate.setSharedReplyTopic(true);
        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(30));
        return replyingKafkaTemplate;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupIdConfig);
        return props;
    }

    @Bean
    public ProducerFactory<String, Object> requestProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ConsumerFactory<String, Object> replyConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, Object> replyListenerContainer(
            ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory) {
        ConcurrentMessageListenerContainer<String, Object> repliesContainer =
                containerFactory.createContainer(REPLY_TOPIC_EMPLOYEE_CREATION);
        repliesContainer.getContainerProperties().setGroupId(REPLY_TOPIC_EMPLOYEE_CREATION);
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }

}

