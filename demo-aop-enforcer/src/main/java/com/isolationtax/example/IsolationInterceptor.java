package com.isolationtax.example;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class IsolationInterceptor {

    private final ExecutionGraph executionGraph;

    public IsolationInterceptor(ExecutionGraph executionGraph) {
        this.executionGraph = executionGraph;
    }

    // This perfectly matches your MathService location!
    @Before("execution(* com.isolationtax.example.MathService.*(..))")
    public void checkIsolationPath(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        executionGraph.verifyNextStep(methodName);
    }
}