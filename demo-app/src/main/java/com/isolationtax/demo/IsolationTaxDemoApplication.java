package com.isolationtax.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Runnable companion to the "isolation-tax" article.
 *
 * <p>It stands up the same {@code OrderService} workflow two ways so the difference between
 * direct peer-to-peer calls and Lateral Isolation is something you can hit with curl instead of
 * just read about:
 *
 * <ul>
 *   <li>{@code GET /demo/without-isolation} — OrderService calls every peer service directly.
 *   <li>{@code GET /demo/with-isolation} — OrderService only ever calls the {@code ServiceGateway};
 *       the gateway is the one place that owns routing, logging, and auth, and the only thing that
 *       talks to the peer services.
 * </ul>
 *
 * Both endpoints return the full call trace as JSON so the "N vs. one door" claim in the article
 * is directly visible in the response rather than asserted.
 */
@SpringBootApplication
public class IsolationTaxDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(IsolationTaxDemoApplication.class, args);
    }
}