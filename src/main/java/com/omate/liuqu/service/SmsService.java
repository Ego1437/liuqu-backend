package com.omate.liuqu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    private final String apiUrl = "https://api.notifyre.com/sms/send";
    private final String apiToken = "qRK8pYysPnWItuR/ucE+xJrn+Fl2ReTUslgM/YOK1xU.au"; // 使用您的实际 API 令牌

    public void sendSms(String phoneNumber, String message) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Body", message);
        requestBody.put("Recipients", new Object[]{ new HashMap<String, String>() {{
            put("type", "mobile_number");
            put("value", phoneNumber);
        }}});
        requestBody.put("From", "");
        requestBody.put("AddUnsubscribeLink", true);

        restTemplate.postForObject(apiUrl, requestBody, String.class);
    }
}
