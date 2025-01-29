package com.midhub.notification_microservice.services;

import com.midhub.notification_microservice.events.OrderItemDtoOutput;

import java.io.IOException;
import java.util.List;

public interface PdfGeneratorService {

    byte[] generateOrderPdf(Long orderId, List<OrderItemDtoOutput> orderItems) throws IOException;
}
