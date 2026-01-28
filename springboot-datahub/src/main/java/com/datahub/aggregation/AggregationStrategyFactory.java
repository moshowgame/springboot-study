package com.datahub.aggregation;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AggregationStrategyFactory {
    private final Map<String, AggregationStrategy> strategies = new HashMap<>();

    public AggregationStrategyFactory() {
        strategies.put("addUp", new AddUpStrategy());
        strategies.put("overwrite", (values) -> values[values.length - 1]);
        strategies.put("merge", (values) -> {
            if (values[0] instanceof Map) {
                Map<String, Object> result = new HashMap<>();
                for (Object value : values) {
                    if (value instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> map = (Map<String, Object>) value;
                        result.putAll(map);
                    }
                }
                return result;
            }
            return values[0];
        });
    }

    public AggregationStrategy getStrategy(String strategyName) {
        AggregationStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown aggregation strategy: " + strategyName);
        }
        return strategy;
    }
}
