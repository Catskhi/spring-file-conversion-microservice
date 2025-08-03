package com.catskhi.mail.service;

import com.catskhi.mail.domain.EmailModel;
import com.catskhi.mail.enums.EmailStatus;
import com.catskhi.mail.repositories.EmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepository emailRepository;

    @Value("EMAIL_USERNAME")
    private String emailFrom;

    @Transactional
    public void sendAndSaveEmail(EmailModel emailModel) {
        emailModel.setStatus(EmailStatus.PENDING);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getEmailSubject());
            message.setText(emailModel.getBody());
            mailSender.send(message);
            emailModel.setStatus(EmailStatus.SENT);
        } catch (Exception e) {
            emailModel.setStatus(EmailStatus.FAILED);
            System.out.println("Failed to send email: " + e.getMessage());
        } finally {
            emailRepository.save(emailModel);
        }
    }
}
