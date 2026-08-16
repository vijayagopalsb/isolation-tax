package com.isolation.gateway.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StepRequest {
    private String name;
    private String service;
    private String method;
    private String path;
    private Map<String, Object> input;
    private String outputKey;
    private String condition;
    private long timeoutMillis = 10000;
    private RetryPolicyRequest retryPolicy;
}