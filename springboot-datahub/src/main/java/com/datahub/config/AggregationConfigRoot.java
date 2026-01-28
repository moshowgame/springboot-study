package com.datahub.config;

import lombok.Data;
import java.util.List;

@Data
public class AggregationConfigRoot {
    private AggregationSection aggregation;

    @Data
    public static class AggregationSection {
        private List<AggregationStrategyConfig> strategies;
    }
}
