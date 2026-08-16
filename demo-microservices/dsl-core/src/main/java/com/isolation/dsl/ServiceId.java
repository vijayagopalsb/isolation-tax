package com.isolation.dsl;

import lombok.Getter;

@Getter
public enum ServiceId {
    SERVICE_A("service-a"),
    SERVICE_B("service-b"),
    SERVICE_C("service-c"),
    SERVICE_D("service-d");

    private final String serviceName;

    ServiceId(String serviceName) {
        this.serviceName = serviceName;
    }

    public static ServiceId fromServiceName(String serviceName) {
        for (ServiceId serviceId : values()) {
            if (serviceId.serviceName.equalsIgnoreCase(serviceName)) {
                return serviceId;
            }
        }
        throw new IllegalArgumentException("Unknown service name: " + serviceName);
    }
}