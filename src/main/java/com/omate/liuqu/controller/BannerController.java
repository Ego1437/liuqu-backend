package com.omate.liuqu.controller;

import com.omate.liuqu.model.Banner;
import com.omate.liuqu.model.Result;
import com.omate.liuqu.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
public class BannerController {

    private static final Logger logger = LoggerFactory.getLogger(BannerController.class);

    @Autowired
    private BannerService bannerService;

    // ...其他方法...

    @PostMapping("/upload")
    public ResponseEntity<Result> addBanner(@RequestBody Banner banner) {
        logger.info("Received banner upload request: {}", banner);
        // 检查Banner对象的属性是否有效
        Result result = new Result();
        if (banner.getImageUrl() == null || banner.getTitle() == null || banner.getLink() == null) {
            logger.error("Missing required parameters");
            result.setResultFailed(6);
            return ResponseEntity.ok(result);
        }

        result.setResultSuccess(0, bannerService.addBanner(banner)); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getActiveBanners")
    public ResponseEntity<Result> getActiveBanners() {

        Result result = new Result();
        result.setResultSuccess(0, bannerService.getBannersActiveNow()); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }
}
