package com.omate.liuqu.controller;

import com.omate.liuqu.model.Order;
import com.omate.liuqu.model.Result;
import com.omate.liuqu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {

        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Result> createOrder(@RequestBody Order order) {
        Order newOrder = orderService.createOrder(order);
        Result result = new Result();
        if(newOrder != null){
            result.setResultSuccess(0, newOrder); // 使用0作为成功代码，您可以根据需要更改这个值

        }else {
            result.setResultFailed(5); // 使用0作为成功代码，您可以根据需要更改这个值
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getOrdersByUserId/{userId}")
    public ResponseEntity<Result> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        Result result = new Result();
        result.setResultSuccess(0, orders); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{orderId}/updateOrderStatusById")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId,
                                               @RequestBody Integer newStatus) {
        Result result = new Result();
        boolean isUpdated = orderService.updateOrderStatus(orderId, newStatus);
        if (isUpdated) {
            result.setResultSuccess(0);
        } else {
            result.setResultSuccess(3);
        }
        return ResponseEntity.ok(result);
    }


}
