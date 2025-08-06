package com.catskhi.user.producer;

import com.catskhi.user.configuration.RabbitMqConfig;
import com.catskhi.user.domain.UserModel;
import com.catskhi.user.dto.EmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    public void sendUserCreateEvent(UserModel userModel) {
        EmailDto dto = new EmailDto();
        dto.setUserId(userModel.getId());
        dto.setEmailTo(userModel.getEmail());
        dto.setEmailSubject("Welcome to the application!");
        dto.setBody("Hello " + userModel.getName() + ",\n\nThank you for registering with us!");

        rabbitTemplate.setMessageConverter(rabbitMqConfig.jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(rabbitMqConfig.emailExchange().getName(), "user-created", dto);
        System.out.println(" [x] Sent: '" + dto + "'");
    }
}
