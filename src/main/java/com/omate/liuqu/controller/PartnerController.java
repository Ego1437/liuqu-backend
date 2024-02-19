package com.omate.liuqu.controller;

import com.omate.liuqu.dto.*;
import com.omate.liuqu.model.*;
import com.omate.liuqu.repository.*;
import com.omate.liuqu.service.*;
import com.omate.liuqu.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.omate.liuqu.dto.ActivityDTO;
import com.omate.liuqu.model.Activity;
import com.omate.liuqu.model.Partner;
import com.omate.liuqu.service.PartnerService;
import jakarta.validation.Valid;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping(value = "/createPartner", consumes = { "multipart/form-data" })
    // @PostMapping("/createPartner")
    public Partner createPartner(@Valid Partner partner, @RequestParam("partnerStaffId") Long partnerStaffId) {

        return partnerService.createPartner(partner, partnerStaffId);
    }

    @PostMapping("/partnerLogin")
    public ResponseEntity<Result> partnerLogin(@RequestParam String phoneNumber, @RequestParam String password) {
        logger.warn("Logining patner with phoneNumber: {}, password: {}", phoneNumber, password);
        Result result = new Result();
        try {
            LoginResponse response = partnerService.partnerLogin(phoneNumber, password);
            if (response.getAccessToken() == "1") {
                result.setResultFailed(1); // 使用0作为成功代码，您可以根据需要更改这个值
            } else {
                result.setResultSuccess(0, response);
            }
            logger.warn("Logining partner with result: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.setResultFailed(10, "Login failed: " + e.getMessage());
            logger.warn("Logining partner with result: {}", result);
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping
    public List<Partner> getAllPartners() {
        return partnerService.getAllPartners();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partner> getPartnerById(@PathVariable Long id) {
        return partnerService.getPartnerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partner> updatePartner(@PathVariable Long id, @RequestBody Partner partner) {
        return partnerService.getPartnerById(id)
                .map(existingPartner -> {
                    partner.setPartnerId(id);
                    return ResponseEntity.ok(partnerService.updatePartner(partner));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        return partnerService.getPartnerById(id)
                .map(partner -> {
                    partnerService.deletePartner(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{partnerId}/activities")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByPartner(@PathVariable Long partnerId) {
        List<ActivityDTO> activities = partnerService.getActivitiesWithDetailsByPartner(partnerId);
        if (activities.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activities);
    }

    @PutMapping("/comfirmOrder")
    public ResponseEntity<Result> confirmOrder(@RequestParam Long partnerId, @RequestParam String orderId,
            @RequestHeader("Authorization") String accessToken) {
        Result result = partnerService.confirmOrder(partnerId, orderId, accessToken);
        if (result.getCode() == 0) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }
}
