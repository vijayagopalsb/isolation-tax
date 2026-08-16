package com.isolation.dsl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Getter
@EqualsAndHashCode
@ToString
public final class Step {

    private final String name;
    private final ServiceId service;
    private final HttpMethod method;
    private final String path;
    private final Map<String, Object> input;
    private final String outputKey;
    private final StepCondition condition;
    private final Duration timeout;
    private final RetryPolicy retryPolicy;

    private Step(Builder builder) {
        this.name = builder.name;
        this.service = Objects.requireNonNull(builder.service, "service");
        this.method = Objects.requireNonNull(builder.method, "method");
        this.path = Objects.requireNonNull(builder.path, "path");
        this.input = Map.copyOf(builder.input);
        this.outputKey = builder.outputKey;
        this.condition = builder.condition;
        this.timeout = Objects.requireNonNull(builder.timeout, "timeout");
        this.retryPolicy = Objects.requireNonNull(builder.retryPolicy, "retryPolicy");
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public Optional<String> getOutputKey() {
        return Optional.ofNullable(outputKey);
    }

    public Optional<StepCondition> getCondition() {
        return Optional.ofNullable(condition);
    }

    public static final class Builder {
        private final String name;
        private ServiceId service;
        private HttpMethod method = HttpMethod.GET;
        private String path = "/";
        private Map<String, Object> input = Map.of();
        private String outputKey;
        private StepCondition condition;
        private Duration timeout = Duration.ofSeconds(10);
        private RetryPolicy retryPolicy = RetryPolicy.none();

        private Builder(String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Step name is required");
            }
            this.name = name;
        }

        public Builder to(ServiceId service) {
            this.service = Objects.requireNonNull(service, "service");
            return this;
        }

        public Builder to(String serviceName) {
            return to(ServiceId.fromServiceName(serviceName));
        }

        public Builder method(HttpMethod method) {
            this.method = Objects.requireNonNull(method, "method");
            return this;
        }

        public Builder get(String path) {

            return method(HttpMethod.GET).path(path);
        }

        public Builder post(String path) {

            return method(HttpMethod.POST).path(path);
        }

        public Builder put(String path) {

            return method(HttpMethod.PUT).path(path);
        }

        public Builder delete(String path) {

            return method(HttpMethod.DELETE).path(path);
        }

        public Builder patch(String path) {

            return method(HttpMethod.PATCH).path(path);
        }

        public Builder path(String path) {
            this.path = Objects.requireNonNull(path, "path");
            return this;
        }

        public Builder input(Map<String, Object> input) {
            this.input = Map.copyOf(Objects.requireNonNull(input, "input"));
            return this;
        }

        public Builder body(Map<String, Object> body) {

            return input(body);
        }

        public Builder storeAs(String outputKey) {
            if (outputKey == null || outputKey.isBlank()) {
                throw new IllegalArgumentException("outputKey must not be blank");
            }
            this.outputKey = outputKey;
            return this;
        }

        public Builder condition(StepCondition condition) {
            this.condition = Objects.requireNonNull(condition, "condition");
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = Objects.requireNonNull(timeout, "timeout");
            return this;
        }

        public Builder retryPolicy(RetryPolicy retryPolicy) {
            this.retryPolicy = Objects.requireNonNull(retryPolicy, "retryPolicy");
            return this;
        }

        public Step build() {
            if (service == null) {
                throw new IllegalStateException("Step service is required");
            }
            return new Step(this);
        }
    }
}