package com.datahub.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegionalApiEndpointConfig {

    private static final Map<String, String> REGION_ENDPOINTS = new HashMap<>();

    static {
        REGION_ENDPOINTS.put("uk", "/api/uk/kpi/totalPosition");
        REGION_ENDPOINTS.put("cn", "/api/cn/kpi/totalPosition");
        REGION_ENDPOINTS.put("in", "/api/in/kpi/totalPosition");
    }

    public static String getEndpointForRegion(String region) {
        return REGION_ENDPOINTS.get(region.toLowerCase());
    }

    public static boolean hasEndpoint(String region) {
        return REGION_ENDPOINTS.containsKey(region.toLowerCase());
    }
}
