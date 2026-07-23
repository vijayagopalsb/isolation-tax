package com.isolationtax.demo.v2facadeunenforced;

import com.isolationtax.demo.subsystems.InventoryService;
import com.isolationtax.demo.subsystems.NotificationService;
import com.isolationtax.demo.subsystems.PaymentService;
import com.isolationtax.demo.subsystems.ShippingService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * VARIANT 2 — A Facade exists, but it does NOT enforce anything.
 *
 * This is the classic GoF Facade: it simplifies the four-subsystem
 * sequence into one clean call. But note what it does NOT do — it does
 * not stop any other class in the codebase from importing
 * ShippingService, PaymentService, etc. directly and calling them
 * instead. See OrderServiceBypass in this same package: it proves the
 * shortcut is still wide open even with this Facade sitting right here.
 *
 * This is the exact nuance from the Facade-vs-Mediator/Lateral-Isolation
 * discussion: Facade makes the easy path easy, it does not forbid the
 * hard path.
 */
@Component
public class OrderFacade {

    private final ShippingService shipping;
    private final PaymentService payment;
    private final InventoryService inventory;
    private final NotificationService notification;

    public OrderFacade(ShippingService shipping, PaymentService payment,
                        InventoryService inventory, NotificationService notification) {
        this.shipping = shipping;
        this.payment = payment;
        this.inventory = inventory;
        this.notification = notification;
    }

    public List<String> placeOrder(String orderId) {
        return List.of(
                inventory.reserveStock(orderId, "SKU-123"),
                payment.charge(orderId, 49.99),
                shipping.scheduleShipment(orderId),
                notification.notifyCustomer(orderId)
        );
    }
}
