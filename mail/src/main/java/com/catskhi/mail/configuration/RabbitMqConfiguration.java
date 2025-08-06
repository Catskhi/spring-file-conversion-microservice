package com.catskhi.mail.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {
    private final String userEmailQueueName = "user-email-queue";
    private final String videoEmailQueueName = "video-email-queue";

    @Bean
    public Queue userEmailQueue() {
        return new Queue(userEmailQueueName, true);
    }

    @Bean Queue videoEmailQueue() {
        return new Queue(videoEmailQueueName, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
