package com.isolationtax.demo.v3facadeenforced;

import com.isolationtax.demo.subsystems.Latency;
import org.springframework.stereotype.Component;

/**
 * Note: no "public" modifier. This class is package-private — the Java
 * compiler itself will refuse to let any class outside this package
 * even import it, let alone call it. This is Lateral Isolation enforced
 * structurally rather than by convention: the shortcut isn't discouraged,
 * it's unreachable.
 */
@Component
class ShippingSubsystem {
    String scheduleShipment(String orderId) {
        Latency.simulateHop();
        return "Shipment scheduled for order " + orderId;
    }
}
