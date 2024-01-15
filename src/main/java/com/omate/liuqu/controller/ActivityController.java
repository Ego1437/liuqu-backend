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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
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
                                                   @RequestParam("staffId") Integer staffId,
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

    @GetMapping("/getFilteredActivities")
    public ResponseEntity<Result> getActivities(
            @RequestParam(required = false) Integer categoryLevel1,
            @RequestParam(required = false) Integer categoryLevel2,
            @RequestParam(required = false) Integer minActivityDuration,
            @RequestParam(required = false) Integer maxActivityDuration,
            @RequestParam(required = false) String activityName,
            @RequestParam(required = false) String activityAddress,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Activity> activityPage = activityService.getFilteredActivities(categoryLevel1, categoryLevel2, minActivityDuration, maxActivityDuration, activityName, activityAddress, tags, pageable, startTime, endTime);
        Result result = new Result();
        result.setResultSuccess(0, activityPage); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    // 通过ID读取单个活动
    @GetMapping("getActivityById/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        try {
            Activity activity = activityService.getActivityById(id);
            return ResponseEntity.ok(activity);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //通过活动地址和活动名称进行模糊搜索
    @GetMapping("/search")
    public ResponseEntity<Result> searchActivities(@RequestParam String keyword) {
        List<Activity> activities = activityService.searchActivitiesByKeyword(keyword);
        Result result = new Result();
        result.setResultSuccess(0, activities); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    // 通过商家ID获取活动信息
    @GetMapping("/getActivitiesByPartner/{partnerId}")
    public ResponseEntity<Result> getActivitiesByPartnerId(@PathVariable Long partnerId) {
        List<Activity> activities = activityService.getActivitiesByPartnerId(partnerId);
        Result result = new Result();
        result.setResultSuccess(0, activities); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }
    // 更新活动
    @PutMapping("/updateActivity/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activityDetails) {
        // 在Activity对象中获取partnerId和staffId
        Long partnerId = activityDetails.getPartner() != null ? activityDetails.getPartner().getPartnerId() : null;
        Integer staffId = activityDetails.getCustomerStaff() != null ? activityDetails.getCustomerStaff().getCustomerStaffId() : null;

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
