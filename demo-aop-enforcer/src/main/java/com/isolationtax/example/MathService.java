package com.isolationtax.example;

import org.springframework.stereotype.Service;

@Service
class MathService {
    
    public int add(int a, int b) { 
        return a + b; 
    }
    
    public int subtract(int a, int b) { 
        return a - b; 
    }
    
    public int multiply(int a, int b) { 
        return a * b; 
    }
    
    public int divide(int a, int b) { 
        return a / b; 
    }
}