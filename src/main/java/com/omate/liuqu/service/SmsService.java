package com.omate.liuqu.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    private final String apiUrl = "https://api.notifyre.com/sms/send";
    private final String apiToken = "qRK8pYysPnWItuR/ucE+xJrn+Fl2ReTUslgM/YOK1xU.au"; // 使用您的实际 API 令牌

    public void sendSms(String phoneNumber, String message) {
        RestTemplate restTemplate = new RestTemplate();

        // 在方法内创建请求头并设置 API 令牌和内容类型
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-token", apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Body", message);
        requestBody.put("Recipients", Collections.singletonList(new HashMap<String, Object>() {{
            put("type", "mobile_number");
            put("value", phoneNumber);
        }}));
        requestBody.put("From", "OmateGlobal");
        requestBody.put("AddUnsubscribeLink", false);

        // 将头部和请求体封装为 HttpEntity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 发送 POST 请求
        restTemplate.postForObject(apiUrl, entity, String.class);
    }
}
