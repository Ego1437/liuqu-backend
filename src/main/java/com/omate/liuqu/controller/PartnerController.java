package com.omate.liuqu.controller;


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

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping(value = "/createPartner", consumes = { "multipart/form-data" })
//    @PostMapping("/createPartner")
    public Partner createPartner(@Valid Partner partner,  @RequestParam("partnerStaffId") Long partnerStaffId) {

        return partnerService.createPartner(partner, partnerStaffId);
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
}
