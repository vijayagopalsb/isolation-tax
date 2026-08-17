package com.isolation.dsl;

import java.time.Duration;

public final class WorkflowDsl {

    private WorkflowDsl() {
    }

    public static Workflow.Builder workflow(String name) {

        return Workflow.builder(name);
    }

    public static Step.Builder step(String name) {

        return Step.builder(name);
    }

    public static StepCondition when(String expression) {

        return StepCondition.when(expression);
    }

    public static StepCondition when(String expression, String description) {
        return StepCondition.when(expression, description);
    }

    public static RetryPolicy retryPolicy(int maxAttempts, Duration backoff) {
        return RetryPolicy.of(maxAttempts, backoff);
    }
}