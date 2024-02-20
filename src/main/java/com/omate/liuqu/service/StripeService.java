package com.omate.liuqu.service;

import com.omate.liuqu.dto.PaymentIntentDTO;
import com.omate.liuqu.model.Order;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import com.omate.liuqu.model.*;
import com.omate.liuqu.repository.OrderRepository;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.repository.UserRepository;
import com.omate.liuqu.repository.EventRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.*;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public Order createOrder(Order order) throws JsonProcessingException {
        boolean updateResult = ticketService.updateResidualNum(order.getTicketId(), order.getQuantity());

        if (updateResult) {
            Order createResult = createStripeOrder(order);
            return createResult;
        } else {
            return null; // 余票不足
        }
    }

    public Order createStripeOrder(Order order) throws JsonProcessingException {
        // 生成随机字符串作为OrderId
        String generatedId = generateNonceStr();
        order.setOrderId(generatedId);

        // 设置Activity与Order的关联
        Long activityId = order.getActivityId();
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));
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

        try {
            // 开始创建PaymentIntent
            PaymentIntent paymentIntent = createPaymentIntent(finalAmount.longValue(), "usd");
            // 更新订单信息
            String orderNo = paymentIntent.getId();
            String orderString = paymentIntent.getClientSecret();
            order.setOrderOmipayNumber(orderNo);
            order.setOrderPayUrl(orderString);
            orderRepository.save(order);
            return order;
        } catch (Exception e) {
            // 订单创建错误
            ticketService.rollbackResidualNum(order.getTicketId(), order.getQuantity());
            throw new ExternalApiException("订单创建失败");
        }
    }

    // 其他涉及Stripe的方法
    public PaymentIntent createPaymentIntent(Long amount, String currency) {
        Stripe.apiKey = stripeApiKey;
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                // Add more configuration as needed
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            System.out.println("PaymentIntent: " + paymentIntent);

            // 创建并返回DTO
            return paymentIntent;
        } catch (StripeException e) {
            // Log the exception (consider using a logger instead of System.out.println)
            System.out.println("Stripe API error: " + e.getMessage());
            // Handle the exception by throwing it or returning a custom response
            throw new RuntimeException("Error creating payment intent", e);
        }
    }

    private String generateNonceStr() {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 15;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(new Random().nextInt(candidateChars.length())));
        }
        return sb.toString();
    }

    public class ExternalApiException extends RuntimeException {
        public ExternalApiException(String message) {
            super(message);
        }
    }
}
