package com.isolationtax.demo.v3facadeenforced;

import com.isolationtax.demo.subsystems.Latency;
import org.springframework.stereotype.Component;

@Component
class PaymentSubsystem {
    String charge(String orderId, double amount) {
        Latency.simulateHop();
        return "Charged $" + amount + " for order " + orderId;
    }
}
