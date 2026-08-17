package com.isolation.dsl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Getter
@EqualsAndHashCode
@ToString
public final class Workflow {

    private final String name;
    private final List<Step> steps;

    private Workflow(Builder builder) {
        this.name = builder.name;
        this.steps = List.copyOf(builder.steps);
    }

    public static Builder builder(String name) {

        return new Builder(name);
    }

    public static final class Builder {
        private final String name;
        private final List<Step> steps = new ArrayList<>();

        private Builder(String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Workflow name is required");
            }
            this.name = name;
        }

        public Builder step(Step step) {
            this.steps.add(Objects.requireNonNull(step, "step"));
            return this;
        }

        public Builder step(String name, Consumer<Step.Builder> stepConfig) {
            Objects.requireNonNull(stepConfig, "stepConfig");
            Step.Builder builder = Step.builder(name);
            stepConfig.accept(builder);
            return step(builder.build());
        }

        public Workflow build() {
            if (steps.isEmpty()) {
                throw new IllegalStateException("At least one step is required");
            }
            return new Workflow(this);
        }
    }
}