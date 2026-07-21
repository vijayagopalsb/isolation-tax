package com.isolationtax.demo.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Records the sequence of service-to-service hops made while handling one request, so the
 * resulting dependency graph can be inspected instead of just claimed.
 */
public class CallTrace {

    /** One edge in the call graph: {@code from} called {@code to}. */
    public record Hop(String from, String to) {
    }

    private final List<Hop> hops = new ArrayList<>();

    public void record(String from, String to) {
        hops.add(new Hop(from, to));
    }

    public List<Hop> hops() {
        return List.copyOf(hops);
    }
}