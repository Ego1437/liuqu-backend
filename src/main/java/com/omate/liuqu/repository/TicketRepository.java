package com.omate.liuqu.repository;

import com.omate.liuqu.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // JpaRepository提供了基本的CRUD操作，你可以根据需要添加自定义查询方法
}
