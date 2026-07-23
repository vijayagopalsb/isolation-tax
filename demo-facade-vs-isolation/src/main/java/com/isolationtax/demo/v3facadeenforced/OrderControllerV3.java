package com.isolationtax.demo.v3facadeenforced;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Notice this controller CANNOT import ShippingSubsystem, PaymentSubsystem,
 * InventorySubsystem, or NotificationSubsystem even if someone tried —
 * they aren't visible outside this package. OrderGateway is the only
 * option. That's not a coding guideline here, it's a compiler error
 * waiting to happen if anyone tries to violate it from another package.
 */
@RestController
public class OrderControllerV3 {

    private final OrderGateway orderGateway;

    public OrderControllerV3(OrderGateway orderGateway) {

        this.orderGateway = orderGateway;
    }

    @GetMapping("/v3/order")
    public Map<String, Object> placeOrder() {
        long start = System.nanoTime();
        var steps = orderGateway.placeOrder("ORD-3001");
        long tookMs = (System.nanoTime() - start) / 1_000_000;

        return Map.of(
                "variant", "v3 — Facade enforced as Lateral Isolation boundary",
                "steps", steps,
                "took_ms", tookMs,
                "note", "The extra ~7ms here vs v1/v2 is the gateway overhead — the measurable isolation tax."
        );
    }
}
