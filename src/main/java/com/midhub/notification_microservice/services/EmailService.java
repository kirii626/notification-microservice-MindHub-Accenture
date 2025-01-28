package com.midhub.notification_microservice.services;

import com.midhub.notification_microservice.events.UserRegisteredEvent;

public interface EmailService {

    void sendEmail(String to, String subject, String text);
}
