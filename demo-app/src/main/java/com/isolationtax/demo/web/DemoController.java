package com.isolationtax.demo.web;

import com.isolationtax.demo.service.CallTrace;
import com.isolationtax.demo.service.OrderServiceDirect;
import com.isolationtax.demo.service.OrderServiceIsolated;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes the same OrderService workflow both ways so the two call graphs can be diffed directly:
 *
 * <pre>
 *   curl localhost:8080/demo/without-isolation
 *   curl localhost:8080/demo/with-isolation
 * </pre>
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    private final OrderServiceDirect orderServiceDirect;
    private final OrderServiceIsolated orderServiceIsolated;

    public DemoController(OrderServiceDirect orderServiceDirect, OrderServiceIsolated orderServiceIsolated) {
        this.orderServiceDirect = orderServiceDirect;
        this.orderServiceIsolated = orderServiceIsolated;
    }

    @GetMapping("/without-isolation")
    public DemoResult withoutIsolation() {
        return toResult("Without Lateral Isolation (direct peer-to-peer)", orderServiceDirect.placeOrder());
    }

    @GetMapping("/with-isolation")
    public DemoResult withIsolation() {
        return toResult("With Lateral Isolation (routed through the Gateway)", orderServiceIsolated.placeOrder());
    }

    private DemoResult toResult(String title, CallTrace trace) {
        Set<String> directDependencies = trace.hops().stream()
                .filter(hop -> hop.from().equals("OrderService"))
                .map(CallTrace.Hop::to)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new DemoResult(title, trace.hops(), directDependencies);
    }
}