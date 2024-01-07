package com.omate.liuqu.repository;

import com.omate.liuqu.model.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
    List<Activity> findAll();
}
