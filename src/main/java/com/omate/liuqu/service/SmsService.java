package com.omate.liuqu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SmsService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendSms() {
        String url = "https://api.notifyre.com/sms/submit"; // 替换为实际的API URL
        String apiKey = "yourApiKey"; // 替换为你的API密钥

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> smsRequest = new HashMap<>();
        smsRequest.put("body", "Hello Test!");
        smsRequest.put("from", ""); // 如果需要，填写发件人信息
        smsRequest.put("recipients", new Map[] {
                new HashMap<String, String>() {{
                    put("type", "SmsNumber");
                    put("value", "+61444444444");
                }}
        });
        smsRequest.put("scheduledDate", null);
        smsRequest.put("addUnsubscribeLink", false);
        smsRequest.put("callbackUrl", "https://mycallback.com/callback");
        smsRequest.put("metadata", new HashMap<String, String>() {{
            put("Key", "Value");
        }});

        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(smsRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // 处理响应
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("SMS sent successfully: " + response.getBody());
        } else {
            System.out.println("Failed to send SMS: " + response.getStatusCode());
        }
    }
}
