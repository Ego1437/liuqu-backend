package com.omate.liuqu.service;

import com.omate.liuqu.model.Banner;
import com.omate.liuqu.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    // ...其他方法...
    public Banner addBanner(Banner banner) {
        return bannerRepository.save(banner);
    }
    public List<Banner> getBannersActiveNow() {
        LocalDateTime now = LocalDateTime.now();
        return bannerRepository.findBannersActiveNow(now);
    }
}
