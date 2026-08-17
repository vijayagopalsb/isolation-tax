package com.isolation.gateway.dto;



import lombok.Data;
import java.util.List;

@Data
public class WorkflowRequest {
    private String name;
    private List<StepRequest> steps;   // StepRequest is our DTO, not java's
}