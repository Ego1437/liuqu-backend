package com.omate.liuqu.service;

import com.omate.liuqu.model.Activity;
import com.omate.liuqu.model.Notification;
import com.omate.liuqu.model.Partner;
import com.omate.liuqu.model.User;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.repository.NotificationRepository;
import com.omate.liuqu.repository.PartnerRepository;
import com.omate.liuqu.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PartnerRepository partnerRepository;


    public Notification saveNotification(Notification notification) {
        // 假设 NotificationDTO 是包含 userId, activityId, partnerId 的数据传输对象
        Optional<User> user = userRepository.findById(notification.getUserId());
        Optional<Activity> activity = activityRepository.findById(notification.getActivityId());
        Optional<Partner> partner = partnerRepository.findById(notification.getPartnerId());

        if(!user.isPresent() || !activity.isPresent() || !partner.isPresent()) {
            throw new EntityNotFoundException("User, Activity, or Partner not found");
        }

        notification.setUser(user.get());
        notification.setActivity(activity.get());
        notification.setPartner(partner.get());
        return notificationRepository.save(notification);
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification updateNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Transactional
    public boolean updateNotificationStatus(List<Long> notificationIds, Integer status) {
        // 检查所有提供的ID是否存在
        for (Long id : notificationIds) {
            notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Notification not found with id " + id));
        }
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        for (Notification notification : notifications) {
            notification.setStatus(status);
        }
        notificationRepository.saveAll(notifications);
        return true;
    }

    public Map<Long, List<Notification>> getNotificationsByUserIdGroupedByPartnerId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream().collect(Collectors.groupingBy(notification -> notification.getPartner().getPartnerId()));
    }
}
