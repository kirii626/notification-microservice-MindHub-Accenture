package com.midhub.notification_microservice.listeners;

import com.midhub.notification_microservice.events.OrderDtoOutput;
import com.midhub.notification_microservice.services.EmailService;
import com.midhub.notification_microservice.services.PdfGeneratorService;
import com.midhub.notification_microservice.utils.UserServiceClient;
import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderCreatedListener {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private UserServiceClient userServiceClient;

    @RabbitListener(queues = "${rabbitmq.queue.order}")
    public void handleOrderCreatedEvent(OrderDtoOutput order) {
        System.out.println("Order received: " + order.getId());
        System.out.println("Searching email's user with ID: " + order.getUserId());

        String userEmail = userServiceClient.getUserEmail(order.getUserId());

        if (userEmail == null) {
            System.err.println("ERROR: Can't obtain user's email");
            return;
        }

        try {
            byte[] pdfContent = pdfGeneratorService.generateOrderPdf(order.getId(), order.getOrderItems());
            emailService.sendOrderEmail(userEmail, pdfContent);
        } catch (IOException | MessagingException e) {
            System.err.println("ERROR making PDF or sending it" + e.getMessage());
        }
    }

}
