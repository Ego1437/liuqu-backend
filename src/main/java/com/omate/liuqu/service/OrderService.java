package com.omate.liuqu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omate.liuqu.dto.PaymentNotificationDTO;
import com.omate.liuqu.model.*;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.repository.EventRepository;
import com.omate.liuqu.repository.OrderRepository;
import com.omate.liuqu.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final String API_URL = "https://www.omipay.com.cn/omipay/api/v2/MakeAPPOrder";
    private static final String SECRET_KEY = "b94dbe95833240198227afca0f13135d";
    private JsonNode jsonNode;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private NotificationService notificationService;


    private boolean verifySignature(String timestamp, String nonceStr, String incomingSign) {
        // 使用相同的方法拼接字符串
        Map<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("m_number", "20002946064");
        queryStringParams.put("timestamp", timestamp);
        queryStringParams.put("nonce_str", nonceStr);
        String calculatedSign = generateSignature(queryStringParams);
        // 对字符串进行MD5加密
        // 将计算出的签名与传入的签名进行比较
        boolean result = calculatedSign.equals(incomingSign);
        return result;
    }

    private boolean isOrderOfCurrentUser(Order order) {
        // 获取当前认证用户
        String currentUsername = getCurrentUsername();
        // 检查订单是否属于该用户
        return order.getUserId().equals(currentUsername);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString(); // 或者处理未认证的情况
        }
    }
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

    private void sendNotificationToUser(Order order) {
        String messageTemplate = order.getEvent().getMessageTemplate().getTemplate();

        // 创建通知对象
        Notification notification = new Notification();
        notification.setUser(order.getUser()); // 假设 Order 实体中有 user 属性
        notification.setMessage(messageTemplate);
        notification.setActivity(order.getActivity()); // 假设 Order 实体中有 activity 属性
        notification.setPartner(order.getPartner()); // 假设 Order 实体中有 partner 属性
        notification.setUserId(order.getUserId()); // 假设 Order 实体中有 user 属性
        notification.setActivityId(order.getActivityId()); // 假设 Order 实体中有 activity 属性
        notification.setPartnerId(order.getPartnerId()); // 假设 Order 实体中有 partner 属性
        // 设置通知的其他必要信息
        notification.setStatus(0);


        // 保存通知
        notificationService.saveNotification(notification);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    // ... 其他方法 ...
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public boolean updateOrderStatus(String orderId, Integer newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 检查订单是否属于当前用户
        if (!isOrderOfCurrentUser(order)) {
            throw new AccessDeniedException("您无权修改此订单");
        }

        order.setOrderStatus(newStatus);
        orderRepository.save(order);

        return true;
    }

    @Transactional
    public boolean processPaymentNotification(PaymentNotificationDTO notification) {
        // 实现您处理支付通知的逻辑
        // 这可能涉及到验证签名、更新订单状态等操作
        if (!verifySignature(notification.getTimestamp().toString(),
                notification.getNonceStr(), notification.getSign())) {
            throw new SecurityException("签名验证失败");
        }
        // ...
        // 验证通过后，根据订单号找到订单
        Optional<Order> optionalOrder = orderRepository.findById(notification.getOutOrderNo());
        if (!optionalOrder.isPresent()) {
            throw new EntityNotFoundException("订单未找到:" + notification.getOutOrderNo());
        }

        // 获取订单实体
        Order order = optionalOrder.get();

        // 更新订单状态和其他信息
        order.setOrderStatus(1); // 假设1表示支付成功
        order.setPayTime(LocalDateTime.now()); // 或使用notification中的支付时间
        order.setExchangeRate(notification.getExchangeRate());
        order.setCnyAmount(notification.getCnyAmount());

        // 发送通知给用户

        sendNotificationToUser(order);
        // 保存订单更新
        orderRepository.save(order);
        // 返回处理结果
        return true;
    }
    public Order createOrder(Order order) throws JsonProcessingException {
        boolean updateResult = ticketService.updateResidualNum(order.getTicketId(), order.getQuantity());

        if(updateResult) {
            Order createResult = createThirdOrder(order);
            return createResult;
        } else {
            return null; // 余票不足
        }
    }

    public Order createThirdOrder(Order order) throws JsonProcessingException {
        // 生成随机字符串作为OrderId
        String generatedId = generateNonceStr();
        order.setOrderId(generatedId);

        // 设置Activity与Order的关联
        Long activityId = order.getActivityId();
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Event event = eventRepository.findById(order.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        order.setUser(user);
        order.setEvent(event);
        order.setActivity(activity);
        order.setPartner(activity.getPartner());
        // 获取 Activity 名称
        String activityName = activity.getActivityName();

        LocalDateTime now = order.getEvent().getStartTime();
        String startTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String orderName = activityName + " " + startTime; // 组合名称
        BigDecimal finalAmount = order.getFinalAmount(); // 总金额
        String orderId = order.getOrderId(); // 自增ID

        RestTemplate restTemplate = new RestTemplate();
        // JSON 请求体的 Map
        // 查询字符串参数的 Map
        Map<String, String> queryStringParams = new HashMap<>();
        queryStringParams.put("m_number", "20002946064");
        queryStringParams.put("timestamp", String.valueOf(System.currentTimeMillis()));
        queryStringParams.put("nonce_str", generateNonceStr());
        queryStringParams.put("order_name", orderName); // 组合后的名称
        queryStringParams.put("currency", "AUD"); // 货币类型
        queryStringParams.put("amount", String.valueOf(finalAmount.intValue())); // Order 实体中的 totalAmount
        queryStringParams.put("notify_url", "http://13.236.138.98:8083/api/notifications/payment"); // 通知地址
        queryStringParams.put("out_order_no", orderId); // Order 实体的自增 ID
        queryStringParams.put("platform", "ALIPAYONLINE"); // 支付平台
//        queryStringParams.put("o_number", String.valueOf(order.getPartnerId())); // 商家id
        queryStringParams.put("o_number", "001"); // 商家id
        queryStringParams.put("app_id", "wx571a1u199z0qc240"); // 商户的AppId
        // 添加签名，假设 generateSignature 已经考虑了所有参数
        queryStringParams.put("sign", generateSignature(queryStringParams));

        // 使用 ObjectMapper 将 JSON 请求体的 Map 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 构建 QueryString
        String queryString = queryStringParams.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // 将查询字符串参数附加到 URL
        String urlWithQueryString = API_URL + "?" + queryString;

        // 发送 GET 请求
        ResponseEntity<String> response = restTemplate.getForEntity(urlWithQueryString, String.class);


        // 处理响应
        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper resultObjectMapper = new ObjectMapper();
            JsonNode jsonNode = resultObjectMapper.readTree(response.getBody());
            try {

                String returnCode = jsonNode.get("return_code").asText();
                if (!"SUCCESS".equals(returnCode)) {
                    String errorCode = jsonNode.get("error_code").asText();
                    String errorMsg = jsonNode.get("error_msg").asText();
                    ticketService.rollbackResidualNum(order.getTicketId(), order.getQuantity());
                    throw new ExternalApiException(errorCode + "订单创建失败: " + errorMsg);
                }
                // 更新订单信息
                String orderNo = jsonNode.get("order_no").asText();
                String orderString = jsonNode.get("order_string").asText();
                order.setOrderOmipayNumber(orderNo);
                order.setOrderPayUrl(orderString);
                orderRepository.save(order);
                return order;

            } catch (Exception e) {
                // JSON解析错误
                ticketService.rollbackResidualNum(order.getTicketId(), order.getQuantity());
                throw new ExternalApiException("订单创建失败: JSON解析错误");
            }
        } else {
            // HTTP错误响应
            ticketService.rollbackResidualNum(order.getTicketId(), order.getQuantity());
            throw new ExternalApiException("订单创建失败: Http响应错误");
        }
    }

    public Order updateOrder(String id, Order orderDetails) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        // Update the order entity here
        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

    public class ExternalApiException extends RuntimeException {
        public ExternalApiException(String message) {
            super(message);
        }
    }

}
