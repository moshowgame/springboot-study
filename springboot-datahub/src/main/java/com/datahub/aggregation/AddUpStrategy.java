package com.datahub.aggregation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public class AddUpStrategy implements AggregationStrategy {
    @Override
    public Object aggregate(Object[] values) {
        if (values == null || values.length == 0) {
            return null;
        }

        // If first value is a number, add all numbers
        if (values[0] instanceof Number) {
            double sum = 0;
            for (Object value : values) {
                if (value instanceof Number) {
                    sum += ((Number) value).doubleValue();
                }
            }
            return sum;
        }

        // If first value is a Map/ObjectNode, merge and add values
        if (values[0] instanceof Map) {
            return aggregateMaps(values);
        }

        if (values[0] instanceof JsonNode && ((JsonNode) values[0]).isObject()) {
            return aggregateJsonObjects(values);
        }

        // Default: return first non-null value
        for (Object value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private Map<String, Object> aggregateMaps(Object[] values) {
        Map<String, Object> result = new HashMap<>();

        for (Object value : values) {
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) value;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String key = entry.getKey();
                    Object val = entry.getValue();

                    if (result.containsKey(key)) {
                        Object existing = result.get(key);
                        result.put(key, aggregate(new Object[]{existing, val}));
                    } else {
                        result.put(key, val);
                    }
                }
            }
        }

        return result;
    }

    private JsonNode aggregateJsonObjects(Object[] values) {
        ObjectNode result = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode();

        for (Object value : values) {
            if (value instanceof JsonNode && ((JsonNode) value).isObject()) {
                JsonNode node = (JsonNode) value;
                node.fields().forEachRemaining(entry -> {
                    String key = entry.getKey();
                    JsonNode nodeValue = entry.getValue();

                    if (result.has(key)) {
                        JsonNode existing = result.get(key);
                        result.set(key, aggregateJsonNodes(existing, nodeValue));
                    } else {
                        result.set(key, nodeValue);
                    }
                });
            }
        }

        return result;
    }

    private JsonNode aggregateJsonNodes(JsonNode node1, JsonNode node2) {
        if (node1.isNumber() && node2.isNumber()) {
            double sum = node1.asDouble() + node2.asDouble();
            if (node1.isLong() && node2.isLong()) {
                return com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.numberNode((long) sum);
            }
            return com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.numberNode(sum);
        }

        if (node1.isObject() && node2.isObject()) {
            ObjectNode result = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode();
            node1.fields().forEachRemaining(entry -> result.set(entry.getKey(), entry.getValue()));
            node2.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode value = entry.getValue();
                if (result.has(key)) {
                    result.set(key, aggregateJsonNodes(result.get(key), value));
                } else {
                    result.set(key, value);
                }
            });
            return result;
        }

        return node1;
    }
}
