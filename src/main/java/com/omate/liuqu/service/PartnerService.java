package com.omate.liuqu.service;

import com.omate.liuqu.dto.*;
import com.omate.liuqu.model.*;
import com.omate.liuqu.repository.ActivityRepository;
import com.omate.liuqu.repository.OrderRepository;
import com.omate.liuqu.repository.PartnerRepository;
import com.omate.liuqu.repository.PartnerStaffRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private final TokenService tokenService;
    private final OrderRepository orderRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public PartnerService(ActivityRepository activityRepository, PartnerRepository partnerRepository,
            PartnerStaffRepository partnerStaffRepository, TicketService ticketService, TokenService tokenService,
            OrderRepository orderRepository) {
        this.partnerRepository = partnerRepository;
        this.partnerStaffRepository = partnerStaffRepository;
        this.activityRepository = activityRepository;
        this.ticketService = ticketService;
        this.tokenService = tokenService;
        this.orderRepository = orderRepository;
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

    public LoginResponse partnerLogin(String phoneNumber, String password) {
        // 根据手机号查找用户
        Partner partner = partnerRepository.findByBusinessTelephone(phoneNumber);

        if (partner == null || !password.equals(partner.getPassword())) {
            String accessToken = "1";
            String refreshToken = "";
            return new LoginResponse(accessToken, refreshToken);
        }

        // 生成JWT访问令牌和刷新令牌
        String accessToken = tokenService.createPartnerAccessToken(partner);
        String refreshToken = tokenService.createPartnerRefreshToken(partner);

        // 存储刷新令牌
        tokenService.storePartnerRefreshToken(refreshToken, partner.getPartnerId());

        // 返回令牌
        return new LoginResponse(accessToken, refreshToken);
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
        CustomerStaff staff = activity.getCustomerStaff();
        CustomerStaffDTO customerStaffDTO = new CustomerStaffDTO(
                staff.getCustomerStaffId(),
                staff.getStaffName(),
                staff.getStaffTelephone(),
                staff.getStaffEmail());

        // 将Event实体转换为EventDTO
        List<EventDTO> eventDTOs = activity.getEvents().stream()
                .map(event -> new EventDTO(
                        event.getEventId(),
                        event.getStartTime(),
                        event.getDeadline(),
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
                activity.getVerificationType(),
                eventDTOs,
                activity.getCollaborators(),
                activity.getFansCount());

        return activityDTO;
    }

    private void loadTicketsForEvents(List<EventDTO> eventDTOs) {
        for (EventDTO eventDTO : eventDTOs) {
            List<TicketDTO> ticketDTOs = ticketService.getTicketsForEvent(eventDTO.getEventId());
            ;
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

    public Result confirmOrder(Long partnerId, String orderId, String accessToken) {
        // 根据accessToken获取partnerId
        Long partnerIdByToken = Long.valueOf(tokenService.getInfoFromToken(accessToken));

        // 根据partnerId查询Partner实体
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new EntityNotFoundException("Partner not found for id " + partnerId));

        Result result = new Result();

        if (partnerIdByToken != partner.getPartnerId()) {
            result.setResultFailed(4);
            return result;
        }

        // 根据orderId查询Order实体
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for id " + orderId));

        if (order.getOrderStatus() != 1 || order.getPartnerId() != partnerIdByToken) {
            result.setResultFailed(11);
            return result;
        }

        // 确认订单
        order.setOrderStatus(4);
        orderRepository.save(order);
        result.setResultSuccess(0);

        return result;
    }
}
