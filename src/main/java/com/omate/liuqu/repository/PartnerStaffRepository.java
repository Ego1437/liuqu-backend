package com.omate.liuqu.repository;

import com.omate.liuqu.model.PartnerStaff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerStaffRepository extends JpaRepository<PartnerStaff, Long> {
    // 这里可以根据需要添加自定义查询方法
}