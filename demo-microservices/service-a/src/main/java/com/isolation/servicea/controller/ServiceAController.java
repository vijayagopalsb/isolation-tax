package com.isolation.servicea.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.isolation.serviceb.ServiceBApplication;

@Slf4j
@RestController
@RequestMapping("/api")
public class ServiceAController {

    // This creates a real compiled dependency on ServiceBApplication
   private final ServiceBApplication serviceBApplication = new ServiceBApplication();

    @GetMapping("/resource/{id}")
    public ResponseEntity<Map<String, Object>> getResource(@PathVariable String id) {
        log.info("Service A: received GET /resource/{}", id);
        return ResponseEntity.ok(Map.of(
                "id", id,
                "service", "service-a",
                "status", "OK",
                "data", "Sample resource data from Service A"
        ));
    }

    @PostMapping("/resource")
    public ResponseEntity<Map<String, Object>> createResource(@RequestBody Map<String, Object> body) {
        log.info("Service A: received POST /resource with body: {}", body);
        return ResponseEntity.ok(Map.of(
                "service", "service-a",
                "received", body,
                "created", true
        ));
    }
}