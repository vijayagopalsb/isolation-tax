package com.isolationtax.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.isolationtax.demo.gateway.ServiceGateway;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class OrderServiceIsolatedTest {

    private final List<PeerService> peerServices = new PeerServiceConfig().peerServices();
    private final OrderServiceIsolated orderService =
            new OrderServiceIsolated(new ServiceGateway(peerServices));

    @Test
    void orderServiceOnlyEverCallsTheGateway() {
        CallTrace trace = orderService.placeOrder();

        Set<String> directTargets = trace.hops().stream()
                .filter(hop -> hop.from().equals("OrderService"))
                .map(CallTrace.Hop::to)
                .collect(Collectors.toSet());

        // One door: regardless of how many peers exist behind it, OrderService has exactly one
        // direct dependency.
        assertThat(directTargets).containsExactly("Gateway");
    }

    @Test
    void gatewayStillReachesEveryPeerOnOrderServicesBehalf() {
        CallTrace trace = orderService.placeOrder();

        List<String> gatewayTargets = trace.hops().stream()
                .filter(hop -> hop.from().equals("Gateway"))
                .map(CallTrace.Hop::to)
                .collect(Collectors.toList());

        assertThat(gatewayTargets).containsExactlyElementsOf(PeerServiceConfig.PEER_NAMES);
    }
}