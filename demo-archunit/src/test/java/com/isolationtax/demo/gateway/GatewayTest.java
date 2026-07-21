package com.isolationtax.demo.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class GatewayTest {

    @Autowired
    private Gateway gateway;

    @Test
    void routesToServiceA() {
        assertThat(gateway.route("A", "ping")).isEqualTo("Service A handled: ping");
    }

    @Test
    void routesToServiceB() {
        assertThat(gateway.route("B", "ping")).isEqualTo("Service B handled: ping");
    }

    @Test
    void routesToServiceC() {
        assertThat(gateway.route("C", "ping")).isEqualTo("Service C handled: ping");
    }

    @Test
    void unknownServiceIdIsRejected() {
        assertThatThrownBy(() -> gateway.route("D", "ping"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
