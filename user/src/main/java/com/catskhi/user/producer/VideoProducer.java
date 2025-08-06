package com.catskhi.user.producer;

import com.catskhi.user.configuration.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VideoProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    public void sendVideoProcessingEvent(String videoId) {
        rabbitTemplate.setMessageConverter(rabbitMqConfig.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(rabbitMqConfig.videoProcessingQueue().getName(), videoId);
        System.out.println(" [x] Sent video processing request for video ID: '" + videoId + "'");
    }
}
