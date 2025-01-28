package com.midhub.notification_microservice.listeners;

import com.midhub.notification_microservice.events.UserRegisteredEvent;
import com.midhub.notification_microservice.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredListener {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.queues.user}")
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            String to = event.getEmail();
            String subject = "Bienvenido a nuestra plataforma";
            String text = "Hola " + event.getUsername() + ",\n\nGracias por registrarte.";

            emailService.sendEmail(to, subject, text);
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
