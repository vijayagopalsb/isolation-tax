package com.isolation.serviced.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ServiceDController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        log.info("Service D: received GET /health");
        return ResponseEntity.ok(Map.of(
                "service", "service-d",
                "health", "OK"
        ));
    }

    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> execute(@RequestBody Map<String, Object> body) {
        log.info("Service D: received POST /execute with body: {}", body);
        return ResponseEntity.ok(Map.of(
                "service", "service-d",
                "executed", true,
                "result", "Execution completed by Service D"
        ));
    }
}