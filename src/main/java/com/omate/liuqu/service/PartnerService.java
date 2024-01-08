package com.omate.liuqu.service;

import com.omate.liuqu.model.Partner;
import com.omate.liuqu.model.PartnerStaff;
import com.omate.liuqu.repository.PartnerRepository;
import com.omate.liuqu.repository.PartnerStaffRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerStaffRepository partnerStaffRepository;

    public PartnerService(PartnerRepository partnerRepository, PartnerStaffRepository partnerStaffRepository) {
        this.partnerRepository = partnerRepository;
        this.partnerStaffRepository = partnerStaffRepository;
    }

    public Partner createPartner(Partner partner, Long partnerStaffId) {
        // 根据 partnerStaffId 查询 PartnerStaff 实体
        PartnerStaff partnerStaff = partnerStaffRepository.findById(partnerStaffId)
                .orElseThrow(() -> new EntityNotFoundException("PartnerStaff not found for id " + partnerStaffId));

        // 设置 Partner 的 partnerStaff
        partner.setPartnerStaff(partnerStaff);

        // 保存 Partner 实体
        return partnerRepository.save(partner);
    }

    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    public Optional<Partner> getPartnerById(Long id) {
        return partnerRepository.findById(id);
    }

    public Partner updatePartner(Partner partner) {
        return partnerRepository.save(partner);
    }

    public void deletePartner(Long id) {
        partnerRepository.deleteById(id);
    }
}

