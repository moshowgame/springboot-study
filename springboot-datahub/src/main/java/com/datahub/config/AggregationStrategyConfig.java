package com.datahub.config;

import lombok.Data;
import java.util.List;

@Data
public class AggregationStrategyConfig {
    private String name;
    private String path;
    private List<String> regions;
    private List<AggregationRule> aggregationRules;
    private String outputPath;

    @Data
    public static class AggregationRule {
        private String path;
        private String strategy;
        private String mergeMode;
    }
}
