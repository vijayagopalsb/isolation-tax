package com.isolationtax.demo.subsystems;

import org.springframework.stereotype.Component;

/**
 * A subsystem service. Deliberately public with no access restrictions —
 * in v1 and v2, ANY class in the codebase can import and call this
 * directly. That's the point: nothing here forbids the shortcut.
 */
@Component
public class ShippingService {
    public String scheduleShipment(String orderId) {
        Latency.simulateHop();
        return "Shipment scheduled for order " + orderId;
    }
}
