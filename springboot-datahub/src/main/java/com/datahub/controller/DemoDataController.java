package com.datahub.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoDataController {

    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    @GetMapping("/uk/kpi/totalPosition")
    public JsonNode getUkKpiTotalPosition() {
        return createKpiResponse("UK", 1000, 999, 800);
    }

    @GetMapping("/cn/kpi/totalPosition")
    public JsonNode getCnKpiTotalPosition() {
        return createKpiResponse("CN", 1500, 1400, 1200);
    }

    @GetMapping("/in/kpi/totalPosition")
    public JsonNode getInKpiTotalPosition() {
        return createKpiResponse("IN", 800, 750, 600);
    }

    private JsonNode createKpiResponse(String region, int today, int yesterday, int avg) {
        ObjectNode response = objectMapper.createObjectNode();
        ObjectNode data = objectMapper.createObjectNode();

        data.put("today", today + random.nextInt(100));
        data.put("yesterday", yesterday + random.nextInt(100));
        data.put("last30daysAvg", avg + random.nextInt(100));

        response.set("data", data);
        response.put("code", 200);
        response.put("msg", "Success - " + region + " Region");

        return response;
    }
}
