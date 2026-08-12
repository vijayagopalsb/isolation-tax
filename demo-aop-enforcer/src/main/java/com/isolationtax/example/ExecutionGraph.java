package com.isolationtax.example;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class ExecutionGraph {
    
    // The approved lateral isolation path
    private final List<String> allowedSequence = List.of("add", "subtract", "multiply", "divide");
    private int currentStepIndex = 0;

    public void verifyNextStep(String methodName) {
        if (currentStepIndex >= allowedSequence.size()) {
            throw new IllegalStateException("Execution path violated: Sequence already completed.");
        }
        
        String expectedMethod = allowedSequence.get(currentStepIndex);
        
        if (!expectedMethod.equals(methodName)) {
            // The Fail-Fast Circuit Breaker
           // throw new IllegalStateException("Lateral Isolation Violation! Expected '"
               // + expectedMethod + "' but got '" + methodName + "'");
            throw new IllegalStateException("TemporalSequenceViolation: Invalid execution sequence. " +
                    "Operations must follow add() -> subtract() -> multiply() -> divide().");
        }
        
        // Move to the next step in the graph
        currentStepIndex++;
    }

    public void reset() {
        currentStepIndex = 0;
    }
}