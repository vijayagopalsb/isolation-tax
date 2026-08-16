package com.isolation.gateway.controller;

import com.isolation.gateway.dto.WorkflowRequest;
import com.isolation.gateway.service.WorkflowExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowExecutor workflowExecutor;

    @PostMapping("/execute")
    public Mono<ResponseEntity<Map<String, Object>>> executeWorkflow(
            @RequestBody WorkflowRequest request) {
        return workflowExecutor.execute(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.badRequest().body(Map.of("error", e.getMessage()))));
    }
}