package com.isolationtax.demo.gateway;

import com.isolationtax.demo.door.ServiceEndpoint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Gateway/Mediator from the article. Deliberately thin: route by
 * serviceId, nothing else. The moment this class starts making
 * service-specific decisions, it has stopped being a gateway and become
 * "a monolith with extra steps" - the article's own warning in
 * "What it costs."
 */
@Service
public class Gateway {

    private final Map<String, ServiceEndpoint> servicesById;

    public Gateway(List<ServiceEndpoint> services) {
        this.servicesById = services.stream()
                .collect(Collectors.toUnmodifiableMap(ServiceEndpoint::serviceId, Function.identity()));
    }

    public String route(String serviceId, String request) {
        ServiceEndpoint service = servicesById.get(serviceId);
        if (service == null) {
            throw new IllegalArgumentException("No service registered for id: " + serviceId);
        }
        return service.handle(request);
    }
}
