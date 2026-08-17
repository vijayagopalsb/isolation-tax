package com.isolation.servicec.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ServiceCController {

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getData() {
        log.info("Service C: received GET /data");
        return ResponseEntity.ok(Map.of(
                "service", "service-c",
                "data", "Sample data from Service C"
        ));
    }

    @PostMapping("/transform")
    public ResponseEntity<Map<String, Object>> transform(@RequestBody Map<String, Object> body) {
        log.info("Service C: received POST /transform with body: {}", body);
        return ResponseEntity.ok(Map.of(
                "service", "service-c",
                "transformed", true,
                "input", body
        ));
    }
}