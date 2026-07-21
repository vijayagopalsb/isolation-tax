package com.isolationtax.demo.service;

import com.isolationtax.demo.gateway.ServiceGateway;
import org.springframework.stereotype.Service;

/**
 * Same workflow as {@link OrderServiceDirect}, but with Lateral Isolation applied: OrderService's
 * only collaborator is {@link ServiceGateway}. It has no idea PaymentService, InventoryService,
 * or any other peer exists — that knowledge lives in exactly one place.
 */
@Service
public class OrderServiceIsolated {

    private final ServiceGateway gateway;

    public OrderServiceIsolated(ServiceGateway gateway) {
        this.gateway = gateway;
    }

    public CallTrace placeOrder() {
        CallTrace trace = new CallTrace();
        for (String target : PeerServiceConfig.PEER_NAMES) {
            gateway.route("OrderService", target, trace);
        }
        return trace;
    }
}