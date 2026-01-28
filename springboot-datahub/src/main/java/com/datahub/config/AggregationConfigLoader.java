package com.datahub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AggregationConfigLoader {

    @Autowired
    private AggregationConfigProperties configProperties;

    private List<AggregationStrategyConfig> aggregationConfigs = new ArrayList<>();

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @PostConstruct
    public void loadConfigs() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource resource = resolver.getResource(configProperties.getFilePath());

            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    AggregationConfigRoot root = yamlMapper.readValue(is, AggregationConfigRoot.class);
                    aggregationConfigs = root.getAggregation().getStrategies();
                    log.info("Loaded {} aggregation strategies", aggregationConfigs.size());
                }
            }
        } catch (IOException e) {
            log.error("Failed to load aggregation config: {}", e.getMessage());
        }
    }

    public List<AggregationStrategyConfig> getAggregationConfigs() {
        return aggregationConfigs;
    }

    public AggregationStrategyConfig getConfigByName(String name) {
        return aggregationConfigs.stream()
                .filter(config -> config.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
