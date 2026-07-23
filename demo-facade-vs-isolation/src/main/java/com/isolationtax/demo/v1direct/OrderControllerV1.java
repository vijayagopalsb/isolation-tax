package com.isolationtax.demo.v1direct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderControllerV1 {

    private final OrderServiceDirect orderService;

    public OrderControllerV1(OrderServiceDirect orderService) {

        this.orderService = orderService;
    }

    @GetMapping("/v1/order")
    public Map<String, Object> placeOrder() {
        long start = System.nanoTime();
        var steps = orderService.placeOrder("ORD-1001");
        long tookMs = (System.nanoTime() - start) / 1_000_000;

        return Map.of(
                "variant", "v1 — direct calls, no Facade, no isolation",
                "steps", steps,
                "took_ms", tookMs
        );
    }
}
