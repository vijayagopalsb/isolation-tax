package com.isolationtax.demo.v2facadeunenforced;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderControllerV2 {

    private final OrderFacade orderFacade;
    private final OrderServiceBypass bypass;

    public OrderControllerV2(OrderFacade orderFacade, OrderServiceBypass bypass) {
        this.orderFacade = orderFacade;
        this.bypass = bypass;
    }

    @GetMapping("/v2/order")
    public Map<String, Object> placeOrder() {
        long start = System.nanoTime();
        var steps = orderFacade.placeOrder("ORD-2001");
        long tookMs = (System.nanoTime() - start) / 1_000_000;

        return Map.of(
                "variant", "v2 — Facade exists, but is not enforced",
                "steps", steps,
                "took_ms", tookMs,
                "note", "Try GET /v2/order/bypass — it skips this Facade entirely, and nothing stops it."
        );
    }

    @GetMapping("/v2/order/bypass")
    public Map<String, Object> bypassFacade() {
        return Map.of(
                "variant", "v2 — deliberately bypassing OrderFacade",
                "result", bypass.expeditedReship("ORD-2001")
        );
    }
}
