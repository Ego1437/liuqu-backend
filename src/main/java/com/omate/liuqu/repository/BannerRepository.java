package com.omate.liuqu.repository;

import com.omate.liuqu.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("SELECT b FROM Banner b WHERE b.startTime <= :now AND b.endTime >= :now")
    List<Banner> findBannersActiveNow(LocalDateTime now);
}
