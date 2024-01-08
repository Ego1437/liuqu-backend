package com.omate.liuqu.controller;

import com.omate.liuqu.dto.ActivityDTO;
import com.omate.liuqu.model.Activity;
import com.omate.liuqu.model.Result;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.service.ActivityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity") // 定义基础路由
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // 创建新活动的端点
    @PostMapping(value = "/createActivity", consumes = { "multipart/form-data" })
    public ResponseEntity<Activity> createActivity(@Valid Activity activity,
                                                   @RequestParam("partnerId") Long partnerId,
                                                   @RequestParam("staffId") Long staffId,
                                                   @RequestParam("tagIds") List<Long> tagIds) {
        Activity createdActivity = activityService.createActivity(activity, partnerId, staffId, tagIds);
        return ResponseEntity.ok(createdActivity);
    }

    @GetMapping("/getAllActivities")
    public ResponseEntity<Result> getAllActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Activity> activityPage = activityService.getAllActivities(pageable);
        Result result = new Result();
        result.setResultSuccess(0, activityPage); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
//        return ResponseEntity.ok(activityPage);
    }

    // 通过ID读取单个活动
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        try {
            Activity activity = activityService.getActivityById(id);
            return ResponseEntity.ok(activity);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 更新活动
    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id,
                                                   @RequestBody Activity activityDetails,
                                                   @RequestParam Long partnerId,
                                                   @RequestParam Long staffId) {
        Activity updatedActivity = activityService.updateActivity(id, activityDetails, partnerId, staffId);
        return ResponseEntity.ok(updatedActivity);
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
