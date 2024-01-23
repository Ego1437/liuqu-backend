package com.omate.liuqu.controller;

import com.omate.liuqu.dto.PaymentNotificationDTO;
import com.omate.liuqu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        // 假设您有一个服务方法来处理支付通知
        boolean isSuccess = orderService.processPaymentNotification(notification);

        // 记录请求体
        logger.info("=====================================================================================");
        logger.info("Received payment notification: {}", notification);

        if (isSuccess) {
            // 返回成功响应
            return ResponseEntity.ok().build();
        } else {
            // 如果处理失败，根据您的业务逻辑返回适当的响应
            return ResponseEntity.badRequest().build();
        }
    }
}
