package com.isolationtax.demo.subsystems;

import org.springframework.stereotype.Component;

@Component
public class PaymentService {
    public String charge(String orderId, double amount) {
        Latency.simulateHop();
        return "Charged $" + amount + " for order " + orderId;
    }
}
