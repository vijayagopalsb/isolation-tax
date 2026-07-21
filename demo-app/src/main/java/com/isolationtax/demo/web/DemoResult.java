package com.isolationtax.demo.web;

import com.isolationtax.demo.service.CallTrace;
import java.util.List;
import java.util.Set;

/**
 * API response for both demo endpoints. {@code orderServiceDirectDependencies} is the point of
 * the whole demo: without isolation it lists all ten peer services; with isolation it lists
 * exactly one thing — {@code Gateway}.
 */
public record DemoResult(
        String title,
        List<CallTrace.Hop> hops,
        Set<String> orderServiceDirectDependencies
) {
}