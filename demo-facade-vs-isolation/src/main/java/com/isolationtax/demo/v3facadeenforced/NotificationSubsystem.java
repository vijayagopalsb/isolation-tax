package com.isolationtax.demo.v3facadeenforced;

import com.isolationtax.demo.subsystems.Latency;
import org.springframework.stereotype.Component;

@Component
class NotificationSubsystem {
    String notifyCustomer(String orderId) {
        Latency.simulateHop();
        return "Customer notified for order " + orderId;
    }
}
