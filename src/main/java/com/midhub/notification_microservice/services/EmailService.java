package com.midhub.notification_microservice.services;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendOrderEmail(String email, byte[] pdfContent) throws MessagingException;
}

