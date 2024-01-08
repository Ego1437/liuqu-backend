package com.omate.liuqu.service;

import com.omate.liuqu.dto.ActivityDTO;
import com.omate.liuqu.model.Activity;
import com.omate.liuqu.model.CustomerStaff;
import com.omate.liuqu.model.Partner;
import com.omate.liuqu.model.Tag;
import com.omate.liuqu.repository.ActivityRepository;

import com.omate.liuqu.repository.CustomerStaffRepository;
import com.omate.liuqu.repository.PartnerRepository;
import com.omate.liuqu.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final TagRepository tagRepository;
    private final ActivityRepository activityRepository;
    private final PartnerRepository partnerRepository; // 你的Partner仓库
    private final CustomerStaffRepository customerStaffRepository; // 你的CustomerStaff仓库

    @Autowired
    public ActivityService(TagRepository tagRepository,
                           ActivityRepository activityRepository,
                           PartnerRepository partnerRepository,
                           CustomerStaffRepository customerStaffRepository) {
        this.activityRepository = activityRepository;
        this.partnerRepository = partnerRepository;
        this.customerStaffRepository = customerStaffRepository;
        this.tagRepository = tagRepository;
    }

    public Activity createActivity(Activity activity, Long partnerId, Long customerStaffId, List<Long> tagIds) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("Partner not found with id " + partnerId));
        CustomerStaff staff = customerStaffRepository.findById(customerStaffId)
                .orElseThrow(() -> new EntityNotFoundException("CustomerStaff not found with id " + customerStaffId));

        // 查询并验证所有标签ID
        Set<Tag> tags = tagRepository.findAllById(tagIds).stream().collect(Collectors.toSet());
        if (tags.size() != tagIds.size()) {
            throw new EntityNotFoundException("One or more tags not found.");
        }

        activity.setPartner(partner);
        activity.setStaff(staff);
        activity.setTags(tags);

        return activityRepository.save(activity);
    }

    public Activity updateActivity(Long activityId, Activity activityDetails, Long partnerId, Long customerStaffId) {
        Activity activity = getActivityById(activityId);

        // Check if partner and staff exist
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("Partner not found with id " + partnerId));
        CustomerStaff staff = customerStaffRepository.findById(customerStaffId)
                .orElseThrow(() -> new EntityNotFoundException("CustomerStaff not found with id " + customerStaffId));

        // Update activity details
        activity.setPartner(partner);
        activity.setStaff(staff);
        // ... update other fields ...

        return activityRepository.save(activity);
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id " + id));
    }

    // 删除特定ID的活动
    public boolean deleteActivity(Long id) {
        try {
            Activity activity = activityRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Activity not found with id " + id));
            activityRepository.delete(activity);
            return true; // 删除成功
        } catch (EntityNotFoundException e) {
            return false; // 活动未找到，删除失败
        }
    }

    @Transactional(readOnly = true)
    public Page<Activity> getAllActivities(Pageable pageable) {
        Page<Activity> page = activityRepository.findAll(pageable);
        // 初始化每个活动的场次和票档
        page.getContent().forEach(activity -> {
            Hibernate.initialize(activity.getEvents());
            activity.getEvents().forEach(event -> Hibernate.initialize(event.getTickets()));
        });
        return page;
    }
}
