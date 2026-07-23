package com.isolationtax.demo.subsystems;

/**
 * Simulates the cost of a real network hop so the three variants produce
 * genuinely different, measurable response times — not just different
 * code shapes. Each subsystem call costs a small fixed amount; the
 * gateway/facade in v3 additionally charges its own "isolation tax" on
 * top, modeling the extra hop a shared boundary adds in a real
 * distributed system.
 */
public final class Latency {
    private Latency() {}

    public static void simulateHop() {

        sleep(4);

    }

    public static void simulateGatewayOverhead() {
        // The extra cost of routing through a shared boundary —
        // this is the literal "tax" the article is about.
        sleep(7);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
