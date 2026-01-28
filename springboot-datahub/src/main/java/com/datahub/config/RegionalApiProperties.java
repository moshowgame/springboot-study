package com.datahub.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "regional.api")
public class RegionalApiProperties {
    private RegionalConfig uk;
    private RegionalConfig cn;
    private RegionalConfig in;

    @Data
    public static class RegionalConfig {
        private String baseUrl;
    }
}
