package com.isolationtax.demo.v3facadeenforced;

import com.isolationtax.demo.subsystems.Latency;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * VARIANT 3 — Facade AS the enforced Lateral Isolation boundary.
 *
 * This is the only public class in this package. The four subsystem
 * classes it wraps are package-private — the compiler guarantees no
 * other package can reach them, not even accidentally. This is the
 * combination the isolation-tax article and the Facade discussion
 * pointed at but didn't fully build: Facade supplies the clean
 * interface, package-privacy supplies the enforcement Facade alone
 * never had.
 *
 * It also charges simulateGatewayOverhead() once — the literal cost the
 * "Isolation Tax" article describes: routing through a shared boundary
 * costs something real, on top of the four subsystem hops themselves.
 * See ArchitectureRulesTest for the automated proof that this isolation
 * actually holds, not just by convention but structurally.
 */
@Component
public class OrderGateway {

    private final ShippingSubsystem shipping;
    private final PaymentSubsystem payment;
    private final InventorySubsystem inventory;
    private final NotificationSubsystem notification;

    public OrderGateway(ShippingSubsystem shipping, PaymentSubsystem payment,
                         InventorySubsystem inventory, NotificationSubsystem notification) {
        this.shipping = shipping;
        this.payment = payment;
        this.inventory = inventory;
        this.notification = notification;
    }

    public List<String> placeOrder(String orderId) {
        Latency.simulateGatewayOverhead(); // the isolation tax itself
        return List.of(
                inventory.reserveStock(orderId, "SKU-123"),
                payment.charge(orderId, 49.99),
                shipping.scheduleShipment(orderId),
                notification.notifyCustomer(orderId)
        );
    }
}
