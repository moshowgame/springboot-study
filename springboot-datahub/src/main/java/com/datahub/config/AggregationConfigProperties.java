package com.datahub.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "aggregation.config")
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class AggregationConfigProperties {
    private boolean enabled;
    private String defaultStrategy;
    private String filePath;
}
