package com.omate.liuqu.service;

import com.omate.liuqu.model.Activity;
import com.omate.liuqu.model.Partner;
import com.omate.liuqu.model.User;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.repository.PartnerRepository;
import com.omate.liuqu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Transactional
    public void addFavorite(Long userId, Long activityId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        activity.setFansCount(activity.getFansCount() + 1);
        activityRepository.save(activity);

        user.getFavoriteActivities().add(activity);
        userRepository.save(user);
    }

    @Transactional
    public void removeFavorite(Long userId, Long activityId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        activity.setFansCount(activity.getFansCount() - 1);
        activityRepository.save(activity);

        user.getFavoriteActivities().remove(activity);
        userRepository.save(user);
    }

    @Transactional
    public void followPartner(Long userId, Long partnerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        partner.setFansCount(partner.getFansCount() + 1);
        partnerRepository.save(partner);

        user.getFollowedPartners().add(partner);
        userRepository.save(user);
    }

    @Transactional
    public void unfollowPartner(Long userId, Long partnerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        partner.setFansCount(partner.getFansCount() - 1);
        partnerRepository.save(partner);

        user.getFollowedPartners().remove(partner);
        userRepository.save(user);
    }
}

