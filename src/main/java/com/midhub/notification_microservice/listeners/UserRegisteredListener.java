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
            System.out.println("❌ Evento recibido sin datos válidos, descartado.");
            return;
        }

        System.out.println("✅ Recibido evento de usuario registrado: " + user.getEmail());

        try {
            sendWelcomeEmail(user.getEmail(), user.getUsername());
        } catch (MessagingException e) {
            System.err.println("❌ Error enviando correo: " + e.getMessage());
        }
    }

    private void sendWelcomeEmail(String email, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("¡Bienvenido a nuestro servicio!");
        helper.setText("<h1>Hola " + name + "!</h1><p>Gracias por registrarte.</p>", true);

        mailSender.send(message);

        System.out.println("Correo enviado a: " + email);
    }
}
