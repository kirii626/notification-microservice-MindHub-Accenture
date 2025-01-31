package com.midhub.notification_microservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailConfigDebug {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @PostConstruct
    public void debugMailConfig() {
        System.out.println("Email configurado: " + username);
        System.out.println("Contraseña (oculta): " + (password != null && !password.isEmpty() ? "OK" : "FALTA"));
    }
}
