package com.example.jettydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.jettydemo")
public class JettyDemoApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(JettyDemoApplication.class, args);
        System.out.println("=================================");
        System.out.println("SpringBoot Jetty Demo Started!");
        System.out.println("Access: http://localhost:8080");
        System.out.println("Health Check: http://localhost:8080/actuator/health");
        System.out.println("=================================");
    }
}