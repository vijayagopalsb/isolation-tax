package com.isolationtax.demo.services.b;

import com.isolationtax.demo.door.ServiceEndpoint;
import org.springframework.stereotype.Component;

@Component
class ServiceB implements ServiceEndpoint {

    @Override
    public String serviceId() {
        return "B";
    }

    @Override
    public String handle(String request) {
        return "Service B handled: " + request;
    }
}
