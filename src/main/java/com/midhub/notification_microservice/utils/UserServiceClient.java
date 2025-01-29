package com.midhub.notification_microservice.utils;

import com.midhub.notification_microservice.config.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public String getUserEmail(Long userId) {
        String url = "http://user-microservice/users/" + userId + "/email"; // 👈 Ajusta el puerto si es necesario
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("❌ ERROR: Can't obtain email from user-microservice: " + e.getMessage());
            return null;
        }
    }
}
