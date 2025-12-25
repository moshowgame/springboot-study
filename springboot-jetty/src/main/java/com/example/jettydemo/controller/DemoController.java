package com.example.jettydemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> helloGet(
            @RequestParam(value = "name", defaultValue = "World") String name,
            @RequestParam(value = "async", defaultValue = "false") boolean async) {

        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello, " + name + "!");
        response.put("timestamp", LocalDateTime.now());
        response.put("server", "Jetty");
        response.put("method", "GET");
        response.put("thread", Thread.currentThread().getName());
        
        return ResponseEntity.ok(response);
    }

}