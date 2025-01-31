package com.midhub.notification_microservice.events;

public class StockReducedEmailEvent {

    private Long productId;
    private String productName;
    private Integer newStock;

    public StockReducedEmailEvent(Long productId, String productName, Integer newStock) {
        this.productId = productId;
        this.productName = productName;
        this.newStock = newStock;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getNewStock() {
        return newStock;
    }
}
