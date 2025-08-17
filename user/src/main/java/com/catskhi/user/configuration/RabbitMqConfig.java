package com.catskhi.user.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    private final String exchangeName = "email";
    private final String videoProcessingQueue = "video-processing-queue";

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue videoProcessingQueue() {
        return new Queue(videoProcessingQueue, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
