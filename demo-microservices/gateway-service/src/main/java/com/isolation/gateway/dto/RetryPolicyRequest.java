package com.isolation.gateway.dto;

import lombok.Data;

@Data
public class RetryPolicyRequest {
    private int maxAttempts = 1;
    private long backoffMillis = 0;
}