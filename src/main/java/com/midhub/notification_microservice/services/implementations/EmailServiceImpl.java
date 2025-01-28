package com.midhub.notification_microservice.services.implementations;

import com.midhub.notification_microservice.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            System.out.println("Correo enviado con éxito a: " + to);
        } catch (MailAuthenticationException e) {
            System.err.println("Error de autenticación: " + e.getMessage());
            e.printStackTrace();
        } catch (MailException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error desconocido: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
