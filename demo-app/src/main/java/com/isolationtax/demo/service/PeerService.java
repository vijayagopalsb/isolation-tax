package com.isolationtax.demo.service;

/**
 * A stand-in for one of the peer services from the article's dependency-sprawl example
 * (PaymentService, InventoryService, ShippingService, ...). Real implementations would call out
 * over HTTP/RPC; here {@link #handle} just does enough to prove it was reached.
 */
public interface PeerService {

    String name();

    default String handle(CallTrace trace) {
        return name() + " processed the request";
    }
}