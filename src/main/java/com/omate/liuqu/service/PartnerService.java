package com.omate.liuqu.service;

import com.omate.liuqu.dto.*;
import com.omate.liuqu.model.*;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.repository.PartnerRepository;
import com.omate.liuqu.repository.PartnerStaffRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerStaffRepository partnerStaffRepository;
    private final ActivityRepository activityRepository;
    private final TicketService ticketService;

    public PartnerService(ActivityRepository activityRepository, PartnerRepository partnerRepository, PartnerStaffRepository partnerStaffRepository, TicketService ticketService) {
        this.partnerRepository = partnerRepository;
        this.partnerStaffRepository = partnerStaffRepository;
        this.activityRepository = activityRepository;
        this.ticketService = ticketService;
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

    public List<ActivityDTO> getActivitiesWithDetailsByPartner(Long partnerId) {
        List<Activity> activities = activityRepository.findActivitiesWithDetailsByPartnerId(partnerId);
        List<ActivityDTO> activityDTOs = activities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // 分别为每个ActivityDTO的每个EventDTO加载TicketDTOs
        for (ActivityDTO activityDTO : activityDTOs) {
            loadTicketsForEvents(activityDTO.getEvents());
        }

        return activityDTOs;
    }


    private ActivityDTO convertToDTO(Activity activity) {
        // 将Tag实体转换为TagDTO
        Set<TagDTO> tagDTOs = activity.getTags().stream()
                .map(tag -> new TagDTO(tag.getTagId(), tag.getTagName()))
                .collect(Collectors.toSet());

        // 将CustomerStaff实体转换为CustomerStaffDTO
        CustomerStaff staff = activity.getStaff();
        CustomerStaffDTO customerStaffDTO = new CustomerStaffDTO(
                staff.getCustomerStaffId(),
                staff.getStaffName(),
                staff.getStaffTelephone(),
                staff.getStaffEmail()
        );

        // 将Event实体转换为EventDTO
        List<EventDTO> eventDTOs = activity.getEvents().stream()
                .map(event -> new EventDTO(
                        event.getEventId(),
                        event.getStartTime(),
                        event.getDeadline(),
                        event.getMaxCapacity(),
                        event.getResidualNum(),
                        event.getEventStatus()
                        // 不立即加载TicketDTOs
                ))
                .collect(Collectors.toList());
        // 基本属性转换
        ActivityDTO activityDTO = new ActivityDTO(
                activity.getActivityId(),
                activity.getActivityAddress(),
                activity.getActivityImage(),
                activity.getActivityName(),
                activity.getActivityDuration(),
                activity.getPortfolio(),
                activity.getActivityDetail(),
                activity.getActivityStatus(),
                activity.getCategoryLevel1(),
                activity.getCategoryLevel2(),
                tagDTOs,
                staff,
                eventDTOs
        );

        return activityDTO;
    }

    private void loadTicketsForEvents(List<EventDTO> eventDTOs) {
        for (EventDTO eventDTO : eventDTOs) {
            List<TicketDTO> ticketDTOs = ticketService.getTicketsForEvent(eventDTO.getEventId());;
            eventDTO.setTickets(ticketDTOs);
        }
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

