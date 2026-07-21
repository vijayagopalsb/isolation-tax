package com.isolationtax.demo.door;

/**
 * The shared contract every service presents to the Gateway. Lives
 * outside the services package on purpose - see the isolation-rules
 * library's README for why that boundary matters.
 */
public interface ServiceEndpoint {
    String serviceId();
    String handle(String request);
}
