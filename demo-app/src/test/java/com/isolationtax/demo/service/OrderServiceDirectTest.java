package com.isolationtax.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class OrderServiceDirectTest {

    private final OrderServiceDirect orderService =
            new OrderServiceDirect(new PeerServiceConfig().peerServices());

    @Test
    void orderServiceCallsEveryPeerDirectly() {
        CallTrace trace = orderService.placeOrder();

        List<String> directTargets = trace.hops().stream()
                .filter(hop -> hop.from().equals("OrderService"))
                .map(CallTrace.Hop::to)
                .collect(Collectors.toList());

        // No shared boundary: OrderService's dependency count equals the number of peers (N).
        assertThat(directTargets).containsExactlyElementsOf(PeerServiceConfig.PEER_NAMES);
        assertThat(directTargets).doesNotContain("Gateway");
    }
}