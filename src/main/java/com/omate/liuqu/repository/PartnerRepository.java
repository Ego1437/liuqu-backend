package com.omate.liuqu.repository;

import com.omate.liuqu.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    // 这里可以根据需要添加自定义查询方法
}
