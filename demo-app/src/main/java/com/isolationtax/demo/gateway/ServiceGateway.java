package com.isolationtax.demo.gateway;

import com.isolationtax.demo.service.CallTrace;
import com.isolationtax.demo.service.PeerService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The shared door from the article: every peer-service call is routed through here, and this is
 * the only place that owns cross-cutting concerns (auth, logging, routing). No caller talks to a
 * peer service directly — see {@code OrderServiceIsolated}, whose only dependency is this class.
 */
@Component
public class ServiceGateway {

    private static final Logger log = LoggerFactory.getLogger(ServiceGateway.class);

    private final Map<String, PeerService> registry;

    public ServiceGateway(List<PeerService> peerServices) {
        this.registry = peerServices.stream()
                .collect(Collectors.toMap(PeerService::name, service -> service));
    }

    public String route(String caller, String target, CallTrace trace) {
        // Cross-cutting concerns live here, and only here.
        log.info("[gateway] {} -> {} (auth checked, request logged)", caller, target);

        PeerService service = registry.get(target);
        if (service == null) {
            throw new IllegalArgumentException("Unknown service: " + target);
        }

        trace.record(caller, "Gateway");
        trace.record("Gateway", target);
        return service.handle(trace);
    }
}