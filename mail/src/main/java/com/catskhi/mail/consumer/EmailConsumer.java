package com.catskhi.mail.consumer;

import com.catskhi.mail.configuration.RabbitMqConfiguration;
import com.catskhi.mail.domain.EmailModel;
import com.catskhi.mail.dto.EmailDto;
import com.catskhi.mail.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @Autowired
    final EmailService emailService;

    @Autowired
    final RabbitMqConfiguration rabbitMqConfig;

    public EmailConsumer(EmailService emailService, RabbitMqConfiguration rabbitMqConfig) {
        this.emailService = emailService;
        this.rabbitMqConfig = rabbitMqConfig;
    }

    @RabbitListener(queues = "user-email-queue")
    public void consumeUserCreatedEvent(@Payload EmailDto emailDto) {
        System.out.println("Received 'created' event from user-email-queue: " + emailDto);
        var email = new EmailModel();
        BeanUtils.copyProperties(emailDto, email);
        emailService.sendAndSaveEmail(email);
        System.out.println("Sent email to: " + email.getEmailTo());
    }

    @RabbitListener(queues = "video-email-queue")
    public void consumeVideoProcessingDoneEvent(@Payload EmailDto emailDto) {
        System.out.println("Received 'video-processing-done' event from video-email-queue: " + emailDto);
        var email = new EmailModel();
        BeanUtils.copyProperties(emailDto, email);
        emailService.sendAndSaveEmail(email);
        System.out.println("Sent video processing done email to: " + email.getEmailTo());
    }
}
