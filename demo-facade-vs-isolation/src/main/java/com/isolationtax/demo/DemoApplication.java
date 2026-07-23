package com.isolationtax.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Isolation Tax — Spring Boot demo.
 *
 * Three variants of the SAME order-placement use case, side by side:
 *
 *   /v1/order   — no Facade, no isolation. OrderControllerV1 talks to
 *                 four subsystem services directly (the "temptation").
 *
 *   /v2/order   — a Facade exists (OrderFacade), but nothing stops
 *                 other code from bypassing it. OrderServiceBypass
 *                 proves the Facade alone does not enforce anything.
 *
 *   /v3/order   — Lateral Isolation enforced: the subsystem classes are
 *                 package-private inside v3facadeenforced, so only
 *                 OrderGateway in that same package can reach them.
 *                 The ArchitectureRulesTest proves this structurally.
 *
 * Run with: mvn spring-boot:run
 * Then call: curl http://localhost:8080/v1/order
 *            curl http://localhost:8080/v2/order
 *            curl http://localhost:8080/v3/order
 */
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
