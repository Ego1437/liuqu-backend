package com.omate.liuqu.service;

import com.omate.liuqu.dto.ActivityDTO;
import com.omate.liuqu.model.Activity;
import com.omate.liuqu.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Activity createActivity(ActivityDTO activityDTO) {
        Activity activity = new Activity();
        // DTO到模型的映射
        // activity.setName(activityDTO.getName());
        // ... 设置其他属性 ...
        return activityRepository.save(activity);
    }

    // 其他服务方法可以在这里定义
    public List<Activity> getAllActivities() {
        return (List<Activity>) activityRepository.findAll();
    }

    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    public Optional<Activity> updateActivity(Long id, ActivityDTO activityDTO) {
        // 实现更新逻辑
        // ...
        return activityRepository.findById(id)
                .map(activity -> {
                    // 假设 ActivityDTO 有一个名为 'name' 的字段
                    if (activityDTO.getActivityName() != null) {
                        activity.setActivityName(activityDTO.getActivityName());
                    }
                    // 继续对其他字段做相同的检查和赋值
                    // ...

                    // 保存更新后的活动记录到数据库
                    return activityRepository.save(activity);
                });
    }

    public boolean deleteActivity(Long id) {
        if (activityRepository.existsById(id)) {
            activityRepository.deleteById(id);
            return true;
        }
        return false;
    }}
