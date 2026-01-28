package com.datahub.aggregation;

public interface AggregationStrategy {
    Object aggregate(Object[] values);
}
