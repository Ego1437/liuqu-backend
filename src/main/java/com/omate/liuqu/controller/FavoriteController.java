package com.omate.liuqu.controller;

import com.omate.liuqu.model.Result;
import com.omate.liuqu.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;




    @PostMapping("/add")
    public ResponseEntity<Result> addFavorite(@RequestParam Long userId, @RequestParam Long activityId) {
        favoriteService.addFavorite(userId, activityId);
        Result result = new Result();
        result.setResultSuccess(0, "Favorite added successfully"); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFavorite(@RequestParam Long userId, @RequestParam Long activityId) {
        favoriteService.removeFavorite(userId, activityId);
        Result result = new Result();
        result.setResultSuccess(0, "Favorite removed successfully"); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    @PostMapping("/follow")
    public ResponseEntity<?> followPartner(@RequestParam Long userId, @RequestParam Long partnerId) {
        favoriteService.followPartner(userId, partnerId);
        Result result = new Result();
        result.setResultSuccess(0, "Follow add successfully"); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unfollowPartner(@RequestParam Long userId, @RequestParam Long partnerId) {
        favoriteService.unfollowPartner(userId, partnerId);
        Result result = new Result();
        result.setResultSuccess(0, "Follow remove successfully"); // 使用0作为成功代码，您可以根据需要更改这个值
        return ResponseEntity.ok(result);
    }
}
