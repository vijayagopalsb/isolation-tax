package com.isolation.dsl;

import lombok.Value;

import java.util.Optional;

@Value
public class StepCondition {

    String expression;
    String description;

    private StepCondition(String expression, String description) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("expression must not be blank");
        }
        this.expression = expression;
        this.description = description;
    }

    public static StepCondition when(String expression) {

        return new StepCondition(expression, null);
    }

    public static StepCondition when(String expression, String description) {
        return new StepCondition(expression, description);
    }

    public Optional<String> getDescription() {

        return Optional.ofNullable(description);
    }
}