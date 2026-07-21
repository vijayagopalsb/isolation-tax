package com.isolationtax.demo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * The "temptation" from the article, made concrete: OrderService reaches directly into every
 * peer service it needs. Each one is a small, reasonable-looking shortcut on its own; together
 * they're the dependency sprawl the "Real-World Example" section describes — ten direct
 * relationships that all have to be understood, mocked, and audited individually.
 *
 * <p>Compare with {@link OrderServiceIsolated}, which does the same work through one door.
 */
@Service
public class OrderServiceDirect {

    private final Map<String, PeerService> registry;

    public OrderServiceDirect(List<PeerService> peerServices) {
        this.registry = peerServices.stream()
                .collect(Collectors.toMap(PeerService::name, service -> service));
    }

    public CallTrace placeOrder() {
        CallTrace trace = new CallTrace();
        for (String target : PeerServiceConfig.PEER_NAMES) {
            // No shared boundary: OrderService knows about, and calls, every peer directly.
            trace.record("OrderService", target);
            registry.get(target).handle(trace);
        }
        return trace;
    }
}