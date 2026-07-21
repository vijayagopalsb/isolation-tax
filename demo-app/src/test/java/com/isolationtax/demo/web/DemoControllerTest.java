package com.isolationtax.demo.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void withoutIsolationExposesTenDirectDependencies() throws Exception {
        mockMvc.perform(get("/demo/without-isolation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderServiceDirectDependencies.length()").value(10));
    }

    @Test
    void withIsolationExposesExactlyOneDirectDependency() throws Exception {
        mockMvc.perform(get("/demo/with-isolation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderServiceDirectDependencies.length()").value(1))
                .andExpect(jsonPath("$.orderServiceDirectDependencies[0]").value("Gateway"));
    }
}