package com.isolationtax.demo.service;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers the peer services from the article's "Real-World Example: Dependency Sprawl" section
 * as a single {@code List<PeerService>} bean, so both {@code OrderServiceDirect} (no isolation)
 * and {@code ServiceGateway} (with isolation) can be wired against the exact same set of peers.
 * That keeps the comparison between the two flows honest: same peers, same work, only the call
 * shape differs.
 */
@Configuration
public class PeerServiceConfig {

    static final List<String> PEER_NAMES = List.of(
            "PaymentService",
            "InventoryService",
            "ShippingService",
            "NotificationService",
            "TaxService",
            "DiscountService",
            "LoyaltyService",
            "FraudDetectionService",
            "AuditService",
            "AnalyticsService"
    );

    @Bean
    public List<PeerService> peerServices() {
        return PEER_NAMES.stream()
                .map(name -> (PeerService) () -> name)
                .toList();
    }
}