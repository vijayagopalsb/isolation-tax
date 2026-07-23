package com.isolationtax.demo.v2facadeunenforced;

import com.isolationtax.demo.subsystems.PaymentService;
import com.isolationtax.demo.subsystems.ShippingService;
import org.springframework.stereotype.Service;

/**
 * Nothing in the language or the build stops this class from existing.
 * OrderFacade is sitting right there, fully capable of handling this —
 * but ShippingService and PaymentService are public, so this class
 * reaches around the Facade anyway. This is exactly the failure mode
 * Lateral Isolation is designed to prevent, and exactly what a plain
 * Facade cannot prevent on its own.
 */
@Service
public class OrderServiceBypass {

    private final ShippingService shipping;
    private final PaymentService payment;

    public OrderServiceBypass(ShippingService shipping, PaymentService payment) {
        this.shipping = shipping;
        this.payment = payment;
    }

    public String expeditedReship(String orderId) {
        // Went straight to the subsystems, skipping OrderFacade entirely.
        payment.charge(orderId, 5.00); // "reship fee"
        return shipping.scheduleShipment(orderId) + " (bypassed the Facade)";
    }
}
