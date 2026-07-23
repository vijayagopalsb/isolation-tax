package com.isolationtax.demo.v3facadeenforced;

import com.isolationtax.demo.subsystems.Latency;
import org.springframework.stereotype.Component;

@Component
class InventorySubsystem {
    String reserveStock(String orderId, String sku) {
        Latency.simulateHop();
        return "Reserved stock of " + sku + " for order " + orderId;
    }
}
