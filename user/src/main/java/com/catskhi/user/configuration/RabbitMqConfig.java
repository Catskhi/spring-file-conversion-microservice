package com.catskhi.user.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    private final String queueName = "email-queue";
    private final String videoProcessingQueue = "video-processing-queue";

    @Bean
    public Queue userQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue videoProcessingQueue() {
        return new Queue(videoProcessingQueue, true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
