package com.omate.liuqu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omate.liuqu.model.Activity;
import com.omate.liuqu.model.Order;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URL;
import java.security.MessageDigest;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final String API_URL = "https://www.omipay.com.cn/omipay/api/v2/MakeAPPOrder";
    private static final String SECRET_KEY = "b94dbe95833240198227afca0f13135d";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TicketService ticketService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }


    // ... 其他方法 ...

    private String generateNonceStr() {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 10 + new Random().nextInt(23);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(new Random().nextInt(candidateChars.length())));
        }
        return sb.toString();
    }

    private String generateSignature(Map<String, String> params) {
        // 将参数按照 key 的字典顺序排序
        String toSign = params.get("m_number") + "&" +
                params.get("timestamp") + "&" +
                params.get("nonce_str") + "&" +
                SECRET_KEY; // 直接使用传入的 secretKey

        // 进行 MD5 加密并转换为大写
        return md5(toSign).toUpperCase();
    }
    private String md5(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5加密出错：" + e.toString());
        }
    }

    public Order createOrder(Order order) {
        boolean updateResult = ticketService.updateResidualNum(order.getTicketId(), order.getQuantity());

        if(updateResult) {
            Order createResult = createThirdOrder(order);
            return createResult;
        } else {
            return null; // 余票不足
        }
    }

    public Order createThirdOrder(Order order){
        String generatedId = generateNonceStr();
        order.setOrderId(generatedId);
        Long activityId = order.getActivityId();
        // 查询 Activity 实体
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        // 获取 Activity 名称
        String activityName = activity.getActivityName();

        String startTime = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String orderName = activityName + " " + startTime; // 组合名称
        BigDecimal totalAmount = order.getTotalAmount(); // 总金额
        String orderId = order.getOrderId(); // 自增ID

        RestTemplate restTemplate = new RestTemplate();
        // JSON 请求体的 Map
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("order_name", orderName); // 组合后的名称
        jsonParams.put("currency", "AUD"); // 货币类型
        jsonParams.put("amount", totalAmount.intValue()); // Order 实体中的 totalAmount
        jsonParams.put("notify_url", ""); // 通知地址
        jsonParams.put("out_order_no", orderId); // Order 实体的自增 ID
        jsonParams.put("platform", "ALIPAYONLINE"); // 支付平台
        jsonParams.put("o_number", order.getPartnerId()); // 商家id
        jsonParams.put("app_id", "wx571a1u199z0qc240"); // 商户的AppId

// QueryString 参数的 Map
        Map<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("m_number", "20002946064");
        queryStringParams.put("timestamp", String.valueOf(System.currentTimeMillis()));
        queryStringParams.put("nonce_str", generateNonceStr());
        queryStringParams.put("sign", generateSignature(queryStringParams));

// 使用 ObjectMapper 将 JSON 请求体的 Map 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = null;
        try {
            jsonRequestBody = objectMapper.writeValueAsString(jsonParams);
        } catch (JsonProcessingException e) {
            // 处理异常
            // 这里可以记录日志，或者根据您的应用程序需求来处理异常
            e.printStackTrace();
        }

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 创建请求实体
        HttpEntity<String> entity = new HttpEntity<>(jsonRequestBody, headers);


        // 构建 QueryString
        String queryString = queryStringParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // 将查询字符串参数附加到 URL
        String urlWithQueryString = API_URL + "?" + queryString;

        // 发送 POST 请求
        ResponseEntity<String> response = restTemplate.postForEntity(urlWithQueryString, entity, String.class);


        // 处理响应
        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper resultObjectMapper = new ObjectMapper();
            try {
                ApiSuccessResponse successResponse = resultObjectMapper.readValue(response.getBody(), ApiSuccessResponse.class);
                if ("SUCCESS".equals(successResponse.getReturnCode())) {
                    // 更新订单信息
                    order.setOrderOmipayNumber(successResponse.getOrderString());
                    order.setOrderPayUrl(successResponse.getOrderNo());
                    orderRepository.save(order);
                    return order;
                } else {
                    // 回滚票档余票数量
                    ticketService.rollbackResidualNum(order.getTicketId(), order.getQuantity());
                    ApiErrorResponse errorResponse = resultObjectMapper.readValue(response.getBody(), ApiErrorResponse.class);
                    throw new ExternalApiException("订单创建失败: " + errorResponse.getErrorMsg());
                }
            } catch (Exception e) {
                // JSON解析错误
                ticketService.rollbackResidualNum(order.getTicketId(), order.getQuantity());
                throw new ExternalApiException("订单创建失败: 解析响应数据出错");
            }
        } else {
            // HTTP错误响应
            ticketService.rollbackResidualNum(order.getTicketId(), order.getQuantity());
            throw new ExternalApiException("订单创建失败: API请求错误");
        }

        // ...
    }

    public class InsufficientTicketsException extends RuntimeException {
        public InsufficientTicketsException(String message) {
            super(message);
        }
    }

    public class ExternalApiException extends RuntimeException {
        public ExternalApiException(String message) {
            super(message);
        }
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        // Update the order entity here
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

    // 你需要定义ApiSuccessResponse和ApiErrorResponse类来匹配API的响应格式
    // 例如:
    public static class ApiSuccessResponse {
        private String returnCode;
        private String orderString;
        private String orderNo;
        // getter和setter

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }

        public String getOrderString() {
            return orderString;
        }

        public void setOrderString(String orderString) {
            this.orderString = orderString;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
    }

    public static class ApiErrorResponse {
        private String returnCode;
        private String errorCode;
        private String errorMsg;
        // getter和setter

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}
