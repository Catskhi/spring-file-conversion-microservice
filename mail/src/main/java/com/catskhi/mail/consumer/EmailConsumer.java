package com.catskhi.mail.consumer;

import com.catskhi.mail.domain.EmailModel;
import com.catskhi.mail.dto.EmailDto;
import com.catskhi.mail.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @Autowired
    final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "email-queue")
    public void consumeEmailQueue(EmailDto emailDto) {
        var email = new EmailModel();
        BeanUtils.copyProperties(emailDto, email);
        emailService.sendAndSaveEmail(email);
        System.out.println("Sent email to: " + emailDto.emailTo());
    }
}
