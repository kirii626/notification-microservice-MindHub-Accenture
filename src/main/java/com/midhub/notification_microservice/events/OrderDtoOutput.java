package com.midhub.notification_microservice.events;

import com.midhub.notification_microservice.events.enums.OrderStatus;

import java.util.List;

public class OrderDtoOutput {

    private Long id;

    private Long userId;

    private OrderStatus orderStatus;

    private List<OrderItemDtoOutput> orderItems;

    public OrderDtoOutput(Long id, Long userId, OrderStatus orderStatus, List<OrderItemDtoOutput> orderItems) {
        this.id = id;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItemDtoOutput> getOrderItems() {
        return orderItems;
    }
}
