package com.midhub.notification_microservice.exceptions;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RabbitMqExceptionHandler implements RabbitListenerErrorHandler {

    private static final Logger logger = Logger.getLogger(RabbitMqExceptionHandler.class.getName());

    @Override
    public Object handleError(Message message, Channel channel, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException exception) throws Exception {
        Throwable cause = exception.getCause();

        if (cause instanceof PdfGenerationException) {
            logger.log(Level.SEVERE, "PDF Generation Error: " + cause.getMessage(), cause);
        } else if (cause instanceof EmailSendingException) {
            logger.log(Level.SEVERE, "Email Sending Error: " + cause.getMessage(), cause);
        } else {
            logger.log(Level.SEVERE, "General Error processing message: " + new String(message.getBody()), cause);
        }
        return null;
    }
}
