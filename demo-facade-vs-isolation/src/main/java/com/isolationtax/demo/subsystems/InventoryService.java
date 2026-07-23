package com.isolationtax.demo.subsystems;

import org.springframework.stereotype.Component;

@Component
public class InventoryService {
    public String reserveStock(String orderId, String sku) {
        Latency.simulateHop();
        return "Reserved stock of " + sku + " for order " + orderId;
    }
}
