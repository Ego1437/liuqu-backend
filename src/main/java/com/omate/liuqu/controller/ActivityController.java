package com.omate.liuqu.controller;

import com.omate.liuqu.dto.ActivityDTO;
import com.omate.liuqu.model.Activity;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activities") // 定义基础路由
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // 创建新活动的端点
    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityDTO activityDTO) {
        Activity createdActivity = activityService.createActivity(activityDTO);
        return ResponseEntity.ok(createdActivity);
    }


    // 读取所有活动
    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    // 通过ID读取单个活动
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        Optional<Activity> activity = activityService.getActivityById(id);
        return activity
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 更新活动
    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody ActivityDTO activityDTO) {
        Optional<Activity> updatedActivity = activityService.updateActivity(id, activityDTO);
        return updatedActivity
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 删除活动
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        boolean isDeleted = activityService.deleteActivity(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
