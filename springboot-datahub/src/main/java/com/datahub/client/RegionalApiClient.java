package com.datahub.client;

import com.datahub.config.RegionalApiEndpointConfig;
import com.datahub.config.RegionalApiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionalApiClient {

    private final RegionalApiProperties regionalApiProperties;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    public Mono<JsonNode> callRegionalApi(String region, String endpoint) {
        String baseUrl = getBaseUrlForRegion(region);
        String regionalEndpoint = getRegionalEndpoint(region);

        String fullUrl = baseUrl + regionalEndpoint;

        log.debug("Calling regional API: {} - URL: {}", region, fullUrl);

        // Create a new WebClient for each call with the full URL
        WebClient webClient = webClientBuilder.build();

        return webClient.get()
                .uri(fullUrl)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseJson)
                .doOnSuccess(response -> log.debug("Success response from {}: {}", region, response))
                .onErrorResume(e -> {
                    log.error("Error calling API {} {}: {}", region, fullUrl, e.getMessage(), e);
                    return Mono.just(createErrorResponse(e.getMessage()));
                });
    }

    private String getRegionalEndpoint(String region) {
        // Use fixed endpoint mapping for each region
        return RegionalApiEndpointConfig.getEndpointForRegion(region);
    }

    public Mono<JsonNode[]> callAllRegions(List<String> regions, String endpoint) {
        return Mono.zip(
                regions.stream()
                        .map(region -> callRegionalApi(region, endpoint))
                        .toList(),
                (values) -> {
                    JsonNode[] responses = new JsonNode[values.length];
                    for (int i = 0; i < values.length; i++) {
                        responses[i] = (JsonNode) values[i];
                    }
                    return responses;
                }
        );
    }

    private String getBaseUrlForRegion(String region) {
        return switch (region.toLowerCase()) {
            case "uk" -> regionalApiProperties.getUk().getBaseUrl();
            case "cn" -> regionalApiProperties.getCn().getBaseUrl();
            case "in" -> regionalApiProperties.getIn().getBaseUrl();
            default -> throw new IllegalArgumentException("Unknown region: " + region);
        };
    }

    private JsonNode parseJson(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            log.error("Failed to parse JSON: {}", e.getMessage());
            return createErrorResponse("Failed to parse response");
        }
    }

    private JsonNode createErrorResponse(String message) {
        try {
            return objectMapper.readTree(
                String.format("{\"data\":null,\"code\":500,\"msg\":\"%s\"}", message)
            );
        } catch (Exception e) {
            return objectMapper.createObjectNode()
                .put("data", (String) null)
                .put("code", 500)
                .put("msg", message);
        }
    }
}
