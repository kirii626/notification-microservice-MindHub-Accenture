package com.midhub.notification_microservice.listeners;

import com.midhub.notification_microservice.events.OrderDtoOutput;
import com.midhub.notification_microservice.exceptions.EmailSendingException;
import com.midhub.notification_microservice.exceptions.PdfGenerationException;
import com.midhub.notification_microservice.services.EmailService;
import com.midhub.notification_microservice.services.PdfGeneratorService;
import com.midhub.notification_microservice.utils.UserServiceClient;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderCreatedListener {

    private static final Logger logger = Logger.getLogger(OrderCreatedListener.class.getName());

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private UserServiceClient userServiceClient;

    @RabbitListener(queues = "${rabbitmq.queue.order}", errorHandler = "rabbitMqExceptionHandler")
    public void handleOrderCreatedEvent(OrderDtoOutput order) {
        logger.info("Order received: " + order.getId());
        logger.info("Searching email's user with ID: " + order.getUserId());

        String userEmail = userServiceClient.getUserEmail(order.getUserId());

        if (userEmail == null) {
            logger.severe("ERROR: Can't obtain user's email");
            return;
        }

        try {
            byte[] pdfContent = pdfGeneratorService.generateOrderPdf(order.getId(), order.getOrderItems());
            emailService.sendOrderEmail(userEmail, pdfContent);
            logger.info("Email sent successfully to: "+ userEmail);
        } catch (IOException e) {
            throw new PdfGenerationException("ERROR generating PDF for order ID: " + order.getId(), e);
        } catch (MessagingException e) {
            throw new EmailSendingException("ERROR sending email to " + userEmail + " for order ID: " + order.getId(), e);
        }
    }

}
