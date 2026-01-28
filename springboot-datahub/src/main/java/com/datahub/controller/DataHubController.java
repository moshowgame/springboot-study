package com.datahub.controller;

import com.datahub.service.DataHubService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import reactor.core.publisher.Mono;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataHubController {

    private final DataHubService dataHubService;

    @GetMapping("/datahub/**")
    public Mono<JsonNode> aggregateData(
            @RequestParam(required = false) String configName,
            HttpServletRequest request
    ) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        log.info("Aggregating data for path: {}", path);

        if (configName != null && !configName.isEmpty()) {
            return dataHubService.aggregateData(configName);
        } else {
            return dataHubService.aggregateDataByEndpoint("/api" + path);
        }
    }

    @GetMapping("/health")
    public Mono<String> health() {
        return Mono.just("{\"status\":\"OK\",\"service\":\"datahub\"}");
    }
}
