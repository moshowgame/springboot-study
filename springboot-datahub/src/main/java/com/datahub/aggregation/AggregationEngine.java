package com.datahub.aggregation;

import com.datahub.config.AggregationStrategyConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregationEngine {

    private final AggregationStrategyFactory strategyFactory;
    private final ObjectMapper objectMapper;

    public JsonNode aggregateResponses(
            JsonNode[] responses,
            AggregationStrategyConfig config
    ) {
        log.debug("Aggregating responses, count: {}", responses.length);
        log.debug("Config: {}", config);
        
        for (int i = 0; i < responses.length; i++) {
            log.debug("Response[{}]: {}", i, responses[i]);
        }
        
        ObjectNode result = objectMapper.createObjectNode();

        for (AggregationStrategyConfig.AggregationRule rule : config.getAggregationRules()) {
            String path = rule.getPath();
            String strategyName = rule.getStrategy();

            log.debug("Processing rule - path: {}, strategy: {}", path, strategyName);

            AggregationStrategy strategy = strategyFactory.getStrategy(strategyName);

            Object[] values = extractValuesFromResponses(responses, path);
            log.debug("Extracted values for path '{}': {} (length: {})", path, values, values.length);

            if (values.length > 0) {
                Object aggregated = strategy.aggregate(values);
                log.debug("Aggregated result: {}", aggregated);
                setAggregatedValue(result, path, aggregated);
            } else {
                log.warn("No values found for path: {}", path);
            }
        }

        return result;
    }

    public JsonNode aggregateToResponseFormat(
            JsonNode[] responses,
            AggregationStrategyConfig config,
            String message
    ) {
        JsonNode aggregatedData = aggregateResponses(responses, config);

        ObjectNode response = objectMapper.createObjectNode();
        response.set("data", aggregatedData);
        response.put("code", 200);
        response.put("msg", message != null ? message : "Success");

        return response;
    }

    private Object[] extractValuesFromResponses(JsonNode[] responses, String path) {
        List<Object> values = new ArrayList<>();

        for (JsonNode response : responses) {
            if (response != null && response.has(path)) {
                JsonNode valueNode = response.get(path);

                if (!valueNode.isNull()) {
                    if (valueNode.isNumber()) {
                        values.add(valueNode.asDouble());
                    } else if (valueNode.isObject()) {
                        values.add(convertJsonNodeToObject(valueNode));
                    } else if (valueNode.isArray()) {
                        values.add(valueNode);
                    } else {
                        values.add(valueNode.asText());
                    }
                }
            }
        }

        return values.toArray();
    }

    private Object convertJsonNodeToObject(JsonNode node) {
        return node;
    }

    private void setAggregatedValue(ObjectNode result, String path, Object aggregated) {
        result.set(path, objectMapper.valueToTree(aggregated));
    }
}
