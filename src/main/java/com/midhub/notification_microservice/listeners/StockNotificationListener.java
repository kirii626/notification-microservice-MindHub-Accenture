package com.midhub.notification_microservice.listeners;

import com.midhub.notification_microservice.events.StockReducedEmailEvent;
import com.midhub.notification_microservice.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockNotificationListener {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.queue.stock.notification}")
    public void handleStockNotification(StockReducedEmailEvent event) {
        System.out.println("Recibido evento de stock reducido: " + event.getProductName() + " - Nuevo Stock: " + event.getNewStock());

        String subject = "Stock reducido: " + event.getProductName();
        String message = "El stock del producto " + event.getProductName() + " (ID: " + event.getProductId() + ") se ha reducido.\n" +
                "Stock actual: " + event.getNewStock();

        emailService.sendEmailToAdmins(subject, message);
    }
}
