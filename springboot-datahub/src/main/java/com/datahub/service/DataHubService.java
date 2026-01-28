package com.datahub.service;

import com.datahub.aggregation.AggregationEngine;
import com.datahub.client.RegionalApiClient;
import com.datahub.config.AggregationConfigLoader;
import com.datahub.config.AggregationStrategyConfig;
import com.datahub.config.AggregationStrategyConfig.AggregationRule;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataHubService {

    private final RegionalApiClient regionalApiClient;
    private final AggregationEngine aggregationEngine;
    private final AggregationConfigLoader configLoader;

    public Mono<JsonNode> aggregateData(String configName) {
        AggregationStrategyConfig config = configLoader.getConfigByName(configName);
        if (config == null) {
            return createErrorResponse("Aggregation config not found: " + configName);
        }

        log.info("Using config: {}", config.getName());
        log.info("Calling regions: {} with path: {}", config.getRegions(), config.getPath());

        return regionalApiClient.callAllRegions(config.getRegions(), config.getPath())
                .doOnNext(responses -> {
                    log.info("Received {} responses from regions", responses.length);
                    for (int i = 0; i < responses.length; i++) {
                        log.info("Region response[{}]: {}", i, responses[i]);
                    }
                })
                .map(responses -> aggregationEngine.aggregateToResponseFormat(
                        responses,
                        config,
                        "Data aggregated successfully"
                ))
                .onErrorResume(e -> {
                    log.error("Aggregation failed: {}", e.getMessage());
                    return createErrorResponse("Aggregation failed: " + e.getMessage());
                });
    }

    public Mono<JsonNode> aggregateDataByEndpoint(String endpoint) {
        log.info("Aggregating for endpoint: {}", endpoint);

        // Try to find matching config
        AggregationStrategyConfig config = configLoader.getAggregationConfigs().stream()
                .filter(c -> endpoint.equals(c.getPath()))
                .findFirst()
                .orElse(null);

        // If no exact match, try to find a pattern match
        if (config == null) {
            // Check if it's a regional endpoint pattern like /api/uk/kpi/totalPosition
            for (AggregationStrategyConfig cfg : configLoader.getAggregationConfigs()) {
                if (isRegionalEndpointMatch(endpoint, cfg.getPath())) {
                    config = cfg;
                    log.info("Found pattern match for endpoint: {} -> config: {}", endpoint, cfg.getName());
                    break;
                }
            }
        }

        if (config != null) {
            AggregationStrategyConfig finalConfig = config;
            return regionalApiClient.callAllRegions(finalConfig.getRegions(), finalConfig.getPath())
                    .doOnNext(responses -> {
                        log.info("Received {} responses from regions", responses.length);
                        for (int i = 0; i < responses.length; i++) {
                            log.info("Region response[{}]: {}", i, responses[i]);
                        }
                    })
                    .map(responses -> aggregationEngine.aggregateToResponseFormat(
                            responses,
                            finalConfig,
                            "Data aggregated successfully"
                    ))
                    .onErrorResume(e -> {
                        log.error("Aggregation failed: {}", e.getMessage());
                        return createErrorResponse("Aggregation failed: " + e.getMessage());
                    });
        }

        // Default configuration
        AggregationStrategyConfig finalConfig = new AggregationStrategyConfig();
        finalConfig.setPath(endpoint);
        finalConfig.setRegions(List.of("uk", "cn", "in"));

        AggregationRule rule = new AggregationRule();
        rule.setPath("data");
        rule.setStrategy("addUp");
        rule.setMergeMode("deep");

        finalConfig.setAggregationRules(List.of(rule));

        log.info("Using default config with endpoint: {}", endpoint);

        return regionalApiClient.callAllRegions(finalConfig.getRegions(), finalConfig.getPath())
                .doOnNext(responses -> {
                    log.info("Received {} responses from regions", responses.length);
                    for (int i = 0; i < responses.length; i++) {
                        log.info("Region response[{}]: {}", i, responses[i]);
                    }
                })
                .map(responses -> aggregationEngine.aggregateToResponseFormat(
                        responses,
                        finalConfig,
                        "Data aggregated successfully"
                ))
                .onErrorResume(e -> {
                    log.error("Aggregation failed: {}", e.getMessage());
                    return createErrorResponse("Aggregation failed: " + e.getMessage());
                });
    }

    private boolean isRegionalEndpointMatch(String requestEndpoint, String configEndpoint) {
        // Check if config path has region placeholder like /api/uk/...
        // and request endpoint matches pattern like /api/uk/... -> should match /api/cn/... and /api/in/...
        String[] configParts = configEndpoint.split("/");
        String[] requestParts = requestEndpoint.split("/");

        if (configParts.length != requestParts.length) {
            return false;
        }

        int matchCount = 0;
        for (int i = 0; i < configParts.length; i++) {
            if (configParts[i].isEmpty() && requestParts[i].isEmpty()) {
                matchCount++;
            } else if (!configParts[i].isEmpty() && !requestParts[i].isEmpty()) {
                // Allow region variations
                if (i == 2 && (requestParts[i].equals("uk") || requestParts[i].equals("cn") || requestParts[i].equals("in"))) {
                    matchCount++;
                } else if (configParts[i].equals(requestParts[i])) {
                    matchCount++;
                }
            }
        }

        return matchCount == configParts.length;
    }

    private Mono<JsonNode> createErrorResponse(String message) {
        return Mono.just(com.fasterxml.jackson.databind.node.JsonNodeFactory.instance
                .objectNode()
                .put("data", (String) null)
                .put("code", 500)
                .put("msg", message));
    }
}
