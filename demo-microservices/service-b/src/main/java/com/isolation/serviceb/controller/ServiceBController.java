package com.isolation.serviceb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ServiceBController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        log.info("Service B: received GET /status");
        return ResponseEntity.ok(Map.of(
                "service", "service-b",
                "status", "UP"
        ));
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> process(@RequestBody Map<String, Object> body) {
        log.info("Service B: received POST /process with body: {}", body);
        return ResponseEntity.ok(Map.of(
                "service", "service-b",
                "processed", true,
                "result", "Processed by Service B"
        ));
    }
}