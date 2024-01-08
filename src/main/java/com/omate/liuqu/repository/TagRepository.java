package com.omate.liuqu.repository;

import com.omate.liuqu.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // 可以在这里添加自定义的查询方法，例如按照标签名称查找等
}
