package com.omate.liuqu.repository;

import com.omate.liuqu.dto.ActivityDTO;
import com.omate.liuqu.model.Activity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
    Page<Activity> findAll(Pageable pageable);

    // 通过商家ID查找活动
    @EntityGraph(attributePaths = {"tags", "customerStaff", "events", "events.tickets"})
    @Query("SELECT a FROM Activity a WHERE a.partner.partnerId = :partnerId")
    List<Activity> findActivitiesWithDetailsByPartnerId(@Param("partnerId") Long partnerId);

    Page<Activity> findAll(Specification<Activity> spec, Pageable pageable);

    @Query("SELECT a FROM Activity a WHERE " +
            "LOWER(a.activityName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.activityAddress) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Activity> findByActivityNameOrActivityAddressContainingIgnoreCase(@Param("keyword") String keyword);
}
