package com.isolationtax.demo.v1direct;

import com.isolationtax.demo.subsystems.InventoryService;
import com.isolationtax.demo.subsystems.NotificationService;
import com.isolationtax.demo.subsystems.PaymentService;
import com.isolationtax.demo.subsystems.ShippingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * VARIANT 1 — No Facade, no isolation.
 *
 * This class depends directly on all four subsystems. This is exactly
 * the "temptation" the isolation-tax article opens with: it's one line
 * of code and a quick hop to just call what you need.
 *
 * The problem this demonstrates: every one of these four dependencies
 * is a real, named, compile-time coupling. Add three more subsystems
 * over the next year (as the article's OrderService dependency-sprawl
 * example describes) and this constructor keeps growing — nobody can
 * look at one file and see the whole integration surface without
 * reading every class that touches an order.
 */
@Service
public class OrderServiceDirect {

    private final ShippingService shipping;
    private final PaymentService payment;
    private final InventoryService inventory;
    private final NotificationService notification;

    public OrderServiceDirect(ShippingService shipping, PaymentService payment,
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
