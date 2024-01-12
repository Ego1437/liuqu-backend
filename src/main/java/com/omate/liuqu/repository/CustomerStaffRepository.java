package com.omate.liuqu.repository;

import com.omate.liuqu.model.CustomerStaff;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerStaffRepository extends CrudRepository<CustomerStaff, Integer> {
    List<CustomerStaff> findAll();
}