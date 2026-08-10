package com.isolationtax;

import com.isolationtax.example.MathService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoAopEnforcerApplication implements CommandLineRunner {

    private final MathService mathService;

    // Injecting the target service
    public  DemoAopEnforcerApplication(MathService mathService) {
        this.mathService = mathService;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=====================================================");
        System.out.println("  STARTING LATERAL ISOLATION DEMO");
        System.out.println("=====================================================\n");

        // ---------------------------------------------------------
        // THE SUCCESS PATH (Following the approved graph)
        // ---------------------------------------------------------
        System.out.println("--- Executing Valid Sequence ---");
        try {
            System.out.println("1. Add: " + mathService.add(10, 5));
            System.out.println("2. Subtract: " + mathService.subtract(10, 5));
            System.out.println("SUCCESS: The Lateral Isolation rules were followed.\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // ---------------------------------------------------------
        // THE FAIL PATH (Violating the approved graph)
        // ---------------------------------------------------------
        System.out.println("--- Attempting Invalid Sequence (Jumping to Divide) ---");
        try {
            // The graph expects "multiply" next, but we are skipping straight to "divide"
            System.out.println("3. Divide: " + mathService.divide(10, 5));
        } catch (Exception e) {
            System.err.println("FATAL EXECUTION ERROR CAUGHT:");
            System.err.println(e.getMessage());
            System.out.println("\nFAIL PATH VERIFIED: The application successfully blocked the peer-to-peer violation!");
        }

        System.out.println("\n=====================================================\n");

    }

    public static void main(String[] args) {

        SpringApplication.run(DemoAopEnforcerApplication.class, args);

    }



}