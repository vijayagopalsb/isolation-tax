package com.isolationtax.demo.subsystems;

import org.springframework.stereotype.Component;

@Component
public class NotificationService {
    public String notifyCustomer(String orderId) {
        Latency.simulateHop();
        return "Customer notified for order " + orderId;
    }
}
