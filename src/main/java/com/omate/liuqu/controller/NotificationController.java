package com.omate.liuqu.controller;

import com.omate.liuqu.model.Notification;
import com.omate.liuqu.model.Result;
import com.omate.liuqu.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/insertNotification")
    public Notification createNotification(@RequestBody Notification notification) {
        return notificationService.saveNotification(notification);
    }

    @GetMapping("/getNotificationByUserId/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @PutMapping("/updateNotifications")
    public ResponseEntity<Result> updateNotificationsStatus(@RequestBody List<Long> notificationIds, @RequestParam Integer status) {
        Result result = new Result();
        notificationService.updateNotificationStatus(notificationIds, status);
        result.setResultSuccess(0); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getNotificationsByUserId/{userId}")
    public ResponseEntity<Map<Long, List<Notification>>> getNotificationsByUserIdGroupedByPartnerId(@PathVariable Long userId) {
        Map<Long, List<Notification>> notificationsGroupedByPartner = notificationService.getNotificationsByUserIdGroupedByPartnerId(userId);
        if (notificationsGroupedByPartner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notificationsGroupedByPartner);
    }
}
