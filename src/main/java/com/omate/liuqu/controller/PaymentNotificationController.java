package com.omate.liuqu.controller;

import com.omate.liuqu.dto.PaymentNotificationDTO;
import com.omate.liuqu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class PaymentNotificationController {

    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentNotificationController.class);

    @Autowired
    public PaymentNotificationController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/payment")
    public ResponseEntity<?> receivePaymentNotification(@RequestBody PaymentNotificationDTO notification) {


        // 记录请求体
        logger.warn("Received payment notification: {}", notification);
        logger.warn("OutOrderNo: "+ notification.getOut_order_no() + "OrderNo: " + notification.getOrder_no() + "NonceStr: " +notification.getNonce_str()
                + "NonceStr: " + notification.getNonce_str() + "Sign: " + notification.getSign() + "CnyAmount: " + notification.getCny_amount()
                + "Currency: " + notification.getCurrency() + "ExchangeRate: " + notification.getExchange_rate() + "OrderTime: " + notification.getOrder_time()
                + "PayTime: " + notification.getPay_time() + "Timestamp: " + notification.getTimestamp() + "TotalAmount: " + notification.getTotal_amount()
                + "ReturnCode: " + notification.getReturn_code());

// 假设您有一个服务方法来处理支付通知
        boolean isSuccess = orderService.processPaymentNotification(notification);
        Map<String, String> response = new HashMap<>();
        if (isSuccess) {
            response.put("return_code", "SUCCESS");
            return ResponseEntity.ok(response);
        } else {
            response.put("return_code", "FAIL");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
