package com.omate.liuqu.repository;

import com.omate.liuqu.model.Activity;
import org.springframework.data.domain.Page;
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
    List<Activity> findByPartner_PartnerId(Long partnerId);
    Page<Activity> findAll(Specification<Activity> spec, Pageable pageable);

}
