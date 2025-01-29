package com.midhub.notification_microservice.listeners;

import com.midhub.notification_microservice.events.UserDtoOutput;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class UserRegisteredListener {

    private final JavaMailSender mailSender;

    public UserRegisteredListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = "${rabbitmq.queue.user}")
    public void handleUserRegisteredEvent(UserDtoOutput user) {
        if (user == null || user.getEmail() == null) {
            System.out.println("Event received with invalid data, discarded");
            return;
        }

        System.out.println("User's registered event received: " + user.getEmail());

        try {
            sendWelcomeEmail(user.getEmail(), user.getUsername());
        } catch (MessagingException e) {
            System.err.println("ERROR sending email" + e.getMessage());
        }
    }

    private void sendWelcomeEmail(String email, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Welcome to our service! :)))");
        helper.setText("<h1>Hi " + name + "!</h1><p>Thank you for registering :D. We are very happy to see you here.</p>", true);

        mailSender.send(message);

        System.out.println("Email sent to: " + email);
    }
}
