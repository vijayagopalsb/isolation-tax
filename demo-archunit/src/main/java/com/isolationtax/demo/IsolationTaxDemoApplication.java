package com.isolationtax.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Runnable companion code for the "Isolation Tax" article.
 *
 * Service A, Service B, and Service C are each isolated in their own
 * package under services/. None of them can reference each other -
 * Java's package-private visibility, plus the ArchUnit test in
 * ServiceIsolationTest, make that a compiler-and-build-enforced fact,
 * not just a documented convention.
 *
 * All three are reachable only through the Gateway, which is the
 * Gateway/Mediator from the article: routing only, no business logic.
 */
@SpringBootApplication
public class IsolationTaxDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(IsolationTaxDemoApplication.class, args);
    }
}
