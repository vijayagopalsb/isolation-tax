package com.isolation.dsl;

import lombok.Value;

import java.time.Duration;

@Value
public class RetryPolicy {

    public static final RetryPolicy NONE = new RetryPolicy(1, Duration.ZERO);

    int maxAttempts;
    Duration backoff;

    private RetryPolicy(int maxAttempts, Duration backoff) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be >= 1");
        }
        if (backoff == null || backoff.isNegative()) {
            throw new IllegalArgumentException("backoff must not be negative");
        }
        this.maxAttempts = maxAttempts;
        this.backoff = backoff;
    }

    public static RetryPolicy none() {
        return NONE;
    }

    public static RetryPolicy of(int maxAttempts, Duration backoff) {

        return new RetryPolicy(maxAttempts, backoff);
    }
}