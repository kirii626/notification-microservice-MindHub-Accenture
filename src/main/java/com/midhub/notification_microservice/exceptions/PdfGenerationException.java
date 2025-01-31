package com.midhub.notification_microservice.exceptions;

public class PdfGenerationException extends RuntimeException {
    public PdfGenerationException(String message, Throwable cause) {
        super(message);
    }
}
