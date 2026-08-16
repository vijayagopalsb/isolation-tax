package com.isolation.gateway.service;

import com.isolation.dsl.*;
import com.isolation.gateway.dto.RetryPolicyRequest;
import com.isolation.gateway.dto.WorkflowRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkflowExecutor {

    private final WebClient webClient;
    private final Map<String, String> serviceUrls;

    public WorkflowExecutor(
            WebClient.Builder webClientBuilder,
            @Value("${gateway.services.service-a:http://localhost:8081}") String serviceAUrl,
            @Value("${gateway.services.service-b:http://localhost:8082}") String serviceBUrl,
            @Value("${gateway.services.service-c:http://localhost:8083}") String serviceCUrl,
            @Value("${gateway.services.service-d:http://localhost:8084}") String serviceDUrl) {
        this.webClient = webClientBuilder.build();
        this.serviceUrls = Map.of(
                "service-a", serviceAUrl,
                "service-b", serviceBUrl,
                "service-c", serviceCUrl,
                "service-d", serviceDUrl
        );
    }

    public Mono<Map<String, Object>> execute(WorkflowRequest request) {
        Workflow workflow = toWorkflow(request);
        Map<String, Object> context = new HashMap<>();
        return executeSteps(workflow.getSteps(), 0, context);
    }

    private Mono<Map<String, Object>> executeSteps(List<Step> steps, int index, Map<String, Object> context) {
        if (index >= steps.size()) {
            return Mono.just(context);
        }

        Step step = steps.get(index);
        return shouldExecute(step, context)
                .flatMap(execute -> {
                    if (!execute) {
                        return executeSteps(steps, index + 1, context);
                    }
                    return executeStep(step, context)
                            .flatMap(result -> {
                                step.getOutputKey().ifPresent(key -> context.put(key, result));
                                return executeSteps(steps, index + 1, context);
                            });
                });
    }

    private Mono<Boolean> shouldExecute(Step step, Map<String, Object> context) {
        return step.getCondition()
                .map(condition -> Mono.fromCallable(() -> evaluateCondition(condition.getExpression(), context)))
                .orElse(Mono.just(true));
    }

    private boolean evaluateCondition(String expression, Map<String, Object> context) {
        String trimmed = expression.trim();

        if (trimmed.contains("==")) {
            String[] parts = trimmed.split("==");
            String left = resolveVariables(parts[0].trim(), context);
            String right = parts[1].trim().replace("'", "");
            return left.equals(right);
        }

        if (trimmed.contains("!=")) {
            String[] parts = trimmed.split("!=");
            String left = resolveVariables(parts[0].trim(), context);
            String right = parts[1].trim().replace("'", "");
            return !left.equals(right);
        }

        return Boolean.parseBoolean(resolveVariables(trimmed, context));
    }

    private Mono<Map<String, Object>> executeStep(Step step, Map<String, Object> context) {
        String url = buildUrl(step);
        Map<String, Object> resolvedInput = resolveInput(step.getInput(), context);

        WebClient.RequestHeadersSpec<?> requestSpec = switch (step.getMethod()) {
            case GET -> webClient.get().uri(url);
            case POST -> webClient.post().uri(url).bodyValue(resolvedInput);
            case PUT -> webClient.put().uri(url).bodyValue(resolvedInput);
            case DELETE -> webClient.delete().uri(url);
            case PATCH -> webClient.patch().uri(url).bodyValue(resolvedInput);
        };

        return requestSpec.retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .timeout(step.getTimeout())
                .retryWhen(Retry.backoff(
                        step.getRetryPolicy().getMaxAttempts() - 1,
                        step.getRetryPolicy().getBackoff()))
                .onErrorMap(e -> new RuntimeException(
                        "Step '" + step.getName() + "' failed: " + e.getMessage(), e));
    }

    private String buildUrl(Step step) {
        String serviceName = step.getService().getServiceName();
        String baseUrl = serviceUrls.getOrDefault(serviceName, "http://localhost:8081");
        return baseUrl + step.getPath();
    }

    private Map<String, Object> resolveInput(Map<String, Object> input, Map<String, Object> context) {
        Map<String, Object> resolved = new HashMap<>();
        input.forEach((key, value) -> {
            if (value instanceof String str) {
                resolved.put(key, resolveVariables(str, context));
            } else {
                resolved.put(key, value);
            }
        });
        return resolved;
    }

    private String resolveVariables(String template, Map<String, Object> context) {
        String result = template;
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return result;
    }

    private Workflow toWorkflow(WorkflowRequest request) {
        Workflow.Builder builder = Workflow.builder(request.getName());

        for (com.isolation.gateway.dto.StepRequest stepReq : request.getSteps()) {
            builder.step(stepReq.getName(), stepBuilder -> {
                stepBuilder.to(ServiceId.fromServiceName(stepReq.getService()));
                stepBuilder.method(HttpMethod.valueOf(stepReq.getMethod().toUpperCase()));
                stepBuilder.path(stepReq.getPath());

                if (stepReq.getInput() != null) {
                    stepBuilder.input(stepReq.getInput());
                }

                if (stepReq.getOutputKey() != null) {
                    stepBuilder.storeAs(stepReq.getOutputKey());
                }

                if (stepReq.getCondition() != null) {
                    stepBuilder.condition(StepCondition.when(stepReq.getCondition()));
                }

                stepBuilder.timeout(Duration.ofMillis(stepReq.getTimeoutMillis()));

                RetryPolicyRequest retry = stepReq.getRetryPolicy();
                if (retry != null) {
                    stepBuilder.retryPolicy(RetryPolicy.of(
                            retry.getMaxAttempts(),
                            Duration.ofMillis(retry.getBackoffMillis())));
                }
            });
        }

        return builder.build();
    }
}