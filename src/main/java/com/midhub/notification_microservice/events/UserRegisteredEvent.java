package com.midhub.notification_microservice.events;


import java.io.Serializable;

public class UserRegisteredEvent implements Serializable {

    private String email;
    private String username;

    // Constructor, Getters y Setters
    public UserRegisteredEvent(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
