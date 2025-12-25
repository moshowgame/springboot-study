package com.example.jettydemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class JettyDemoApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("=== SpringBoot Jetty Demo Test Passed ===");
    }
}