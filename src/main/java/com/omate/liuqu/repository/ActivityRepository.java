package com.omate.liuqu.repository;

import com.omate.liuqu.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
    Page<Activity> findAll(Pageable pageable);
}
