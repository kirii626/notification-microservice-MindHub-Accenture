package com.midhub.notification_microservice.listeners;

import com.midhub.notification_microservice.events.StockReducedEmailEvent;
import com.midhub.notification_microservice.exceptions.EmailSendingException;
import com.midhub.notification_microservice.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class StockNotificationListener {

    private static final Logger logger = Logger.getLogger(StockNotificationListener.class.getName());

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.queue.stock.notification}", errorHandler = "rabbitMQExceptionHandler")
    public void handleStockNotification(StockReducedEmailEvent event) {
        logger.info("Received stock reduction event: " + event.getProductName() + " - New Stock: " + event.getNewStock());

        String subject = "Stock Reduced: " + event.getProductName();
        String message = "The stock for product " + event.getProductName() + " (ID: " + event.getProductId() + ") has been reduced.\n" +
                "Current stock level: " + event.getNewStock();

        try {
            emailService.sendEmailToAdmins(subject, message);
            logger.info("Email successfully sent to administrators.");
        } catch (Exception e) {
            throw new EmailSendingException("ERROR sending stock reduction email for: " + event.getProductName(), e);
        }
    }
}
