package com.isolationtax.demo.services.c;

import com.isolationtax.demo.door.ServiceEndpoint;
import org.springframework.stereotype.Component;

@Component
class ServiceC implements ServiceEndpoint {

    @Override
    public String serviceId() {
        return "C";
    }

    @Override
    public String handle(String request) {
        return "Service C handled: " + request;
    }
}
