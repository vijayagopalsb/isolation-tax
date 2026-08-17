package com.isolation.dsl;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkflowDslTest {

    @Test
    void shouldBuildWorkflowWithSteps() {
        Workflow workflow = WorkflowDsl.workflow("order-processing")
                .step("fetch-order", step -> step
                        .to(ServiceId.SERVICE_A)
                        .get("/orders/123")
                        .storeAs("order"))
                .step("validate-order", step -> step
                        .to(ServiceId.SERVICE_B)
                        .post("/validation")
                        .body(Map.of("orderId", "${order.id}"))
                        .condition(WorkflowDsl.when("${order.status} == 'PENDING'"))
                        .timeout(Duration.ofSeconds(5))
                        .retryPolicy(WorkflowDsl.retryPolicy(3, Duration.ofMillis(500))))
                .build();

        assertThat(workflow.getName()).isEqualTo("order-processing");
        assertThat(workflow.getSteps()).hasSize(2);

        Step firstStep = workflow.getSteps().get(0);
        assertThat(firstStep.getService()).isEqualTo(ServiceId.SERVICE_A);
        assertThat(firstStep.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(firstStep.getPath()).isEqualTo("/orders/123");
        assertThat(firstStep.getOutputKey()).contains("order");

        Step secondStep = workflow.getSteps().get(1);
        assertThat(secondStep.getCondition()).isPresent();
        assertThat(secondStep.getCondition().get().getExpression())
                .isEqualTo("${order.status} == 'PENDING'");
        assertThat(secondStep.getRetryPolicy().getMaxAttempts()).isEqualTo(3);
    }

    @Test
    void shouldRejectMissingService() {
        assertThatThrownBy(() -> Step.builder("bad-step")
                .get("/test")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("service");
    }

    @Test
    void shouldParseServiceName() {
        assertThat(ServiceId.fromServiceName("service-c")).isEqualTo(ServiceId.SERVICE_C);
        assertThatThrownBy(() -> ServiceId.fromServiceName("unknown"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}