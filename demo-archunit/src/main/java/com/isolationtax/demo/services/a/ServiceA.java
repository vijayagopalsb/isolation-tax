package com.isolationtax.demo.services.a;

import com.isolationtax.demo.door.ServiceEndpoint;
import org.springframework.stereotype.Component;

// Violation
import com.isolationtax.demo.services.c.ServiceC;

@Component
class ServiceA implements ServiceEndpoint {

    // Violation test
    ServiceC serviceC;

    @Override
    public String serviceId() {
        return "A";
    }

    @Override
    public String handle(String request) {
        return "Service A handled: " + request;
    }
}
