package com.isolation.dsl;

import java.util.Map;

public class DemoRunner {
    public static void main(String[] args) {
        Workflow workflow = WorkflowDsl.workflow("demo-flow")
                .step("call-a", step -> step
                        .to(ServiceId.SERVICE_A)
                        .get("/api/resource")
                        .storeAs("aResult"))
                .step("call-b", step -> step
                        .to(ServiceId.SERVICE_B)
                        .post("/api/process")
                        .body(Map.of("input", "${aResult}")))
                .build();

        System.out.println(workflow);
    }
}