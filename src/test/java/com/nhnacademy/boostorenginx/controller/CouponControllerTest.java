package com.nhnacademy.boostorenginx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @MockBean
    private CouponService couponService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CouponPolicy mockPolicy;
    private Coupon mockCoupon;
    private CouponCreateRequestDto createRequestDto;
    private CouponResponseDto responseDto;

    private Page<CouponResponseDto> mockExpiredCoupons;
    private Page<CouponResponseDto> mockActiveCoupons;
    private Page<CouponResponseDto> mockPolicyCoupons;
    private Page<CouponResponseDto> mockStatusCoupons;

    private CouponUpdateExpiredRequestDto requestDto;
    private Page<CouponResponseDto> mockResponse;
    private MemberCouponUseRequestDto useRequestDto;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        createRequestDto = new CouponCreateRequestDto(1L, now.plusDays(10));

        responseDto = new CouponResponseDto(
                1L,
                "CODE123",
                Status.UNUSED,
                now,
                now.plusDays(10),
                1L
        );

        mockPolicy = CouponPolicy.builder()
                .name("Holiday Discount")
                .saleType(SaleType.RATIO)
                .minimumAmount(BigDecimal.valueOf(100))
                .discountLimit(BigDecimal.valueOf(500))
                .discountRatio(10)
                .isStackable(true)
                .couponScope("ALL")
                .startDate(LocalDateTime.of(2024, 12, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59))
                .couponActive(true)
                .build();

        mockCoupon = new Coupon(
                Status.UNUSED,
                LocalDateTime.of(2024, 12, 20, 10, 0, 0),
                LocalDateTime.of(2024, 12, 30, 23, 59, 59),
                mockPolicy
        );

        List<CouponResponseDto> couponResponseDtoList = Arrays.asList(
                new CouponResponseDto(1L, "CODE1", Status.EXPIRED, now.minusDays(10), now.minusDays(5), 1L),
                new CouponResponseDto(2L, "CODE2", Status.EXPIRED, now.minusDays(20), now.minusDays(15), 2L)
        );
        mockExpiredCoupons = new PageImpl<>(couponResponseDtoList, PageRequest.of(0, 10), couponResponseDtoList.size());

        List<CouponResponseDto> couponList = Arrays.asList(
                new CouponResponseDto(1L, "ACTIVE1", Status.UNUSED, LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5), 10L),
                new CouponResponseDto(2L, "ACTIVE2", Status.UNUSED, LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(10), 20L)
        );
        mockActiveCoupons = new PageImpl<>(couponList, PageRequest.of(0, 10), couponList.size());

        requestDto = new CouponUpdateExpiredRequestDto(
                LocalDateTime.of(2023, 12, 31, 23, 59, 59), // expiredDate
                "UNUSED",
                0,
                10
        );

        List<CouponResponseDto> expiredCouponList = Arrays.asList(
                new CouponResponseDto(1L, "CODE123", Status.EXPIRED, LocalDateTime.now().minusDays(10), LocalDateTime.now(), 100L),
                new CouponResponseDto(2L, "CODE456", Status.EXPIRED, LocalDateTime.now().minusDays(20), LocalDateTime.now(), 101L)
        );
        mockResponse = new PageImpl<>(expiredCouponList, PageRequest.of(0, 10), expiredCouponList.size());

        useRequestDto = new MemberCouponUseRequestDto(1L, 1L);
    }

    @DisplayName("쿠폰생성 기능")
    @Test
    void createCoupon() throws Exception {
        when(couponService.createCoupon(createRequestDto)).thenReturn(responseDto);
        mockMvc.perform(post("/api/coupons")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.couponId").value(1));
    }

    @DisplayName("쿠폰코드 검색")
    @Test
    void getCouponByCode() throws Exception {
        when(couponService.getCouponByCode(any(CouponCodeRequestDto.class))).thenReturn(mockCoupon);

        mockMvc.perform(get("/api/coupons/code")
                        .param("code", "CODE123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockCoupon.getId()))
                .andExpect(jsonPath("$.code").value(mockCoupon.getCode()))
                .andExpect(jsonPath("$.status").value(mockCoupon.getStatus().toString()))
                .andExpect(jsonPath("$.issuedAt").value("2024-12-20T10:00:00"))
                .andExpect(jsonPath("$.expiredAt").value("2024-12-30T23:59:59"))
                .andExpect(jsonPath("$.couponPolicy.id").value(mockPolicy.getId()))
                .andExpect(jsonPath("$.couponPolicy.name").value(mockPolicy.getName()))
                .andExpect(jsonPath("$.couponPolicy.saleType").value(mockPolicy.getSaleType().toString()))
                .andExpect(jsonPath("$.couponPolicy.minimumAmount").value(mockPolicy.getMinimumAmount()))
                .andExpect(jsonPath("$.couponPolicy.discountLimit").value(mockPolicy.getDiscountLimit()))
                .andExpect(jsonPath("$.couponPolicy.discountRatio").value(mockPolicy.getDiscountRatio()))
                .andExpect(jsonPath("$.couponPolicy.isStackable").value(mockPolicy.isStackable()))
                .andExpect(jsonPath("$.couponPolicy.couponScope").value(mockPolicy.getCouponScope()))
                .andExpect(jsonPath("$.couponPolicy.startDate").value("2024-12-01T00:00:00"))
                .andExpect(jsonPath("$.couponPolicy.endDate").value("2024-12-31T23:59:59"))
                .andExpect(jsonPath("$.couponPolicy.couponActive").value(mockPolicy.isCouponActive()));
    }

    @DisplayName("만료된 쿠폰목록 검색")
    @Test
    void getExpiredCoupons() throws Exception {
        LocalDateTime expiredAt = LocalDateTime.of(2023, 12, 31, 23, 59, 59);

        Mockito.when(couponService.getExpiredCoupons(any(CouponExpiredRequestDto.class)))
                .thenReturn(mockExpiredCoupons);

        mockMvc.perform(get("/api/coupons/expired")
                        .param("expiredAt", expiredAt.toString())
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].code").value("CODE1"))
                .andExpect(jsonPath("$.content[0].status").value("EXPIRED"))
                .andExpect(jsonPath("$.content[0].issuedAt").exists())
                .andExpect(jsonPath("$.content[0].expiredAt").exists())
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].code").value("CODE2"))
                .andExpect(jsonPath("$.content[1].status").value("EXPIRED"))
                .andExpect(jsonPath("$.content[1].issuedAt").exists())
                .andExpect(jsonPath("$.content[1].expiredAt").exists());
    }

    @DisplayName("활성화된 쿠폰목록 검색")
    @Test
    void getActiveCoupons() throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.of(2023, 12, 20, 12, 0, 0);

        Mockito.when(couponService.getActiveCoupons(any(CouponActiveRequestDto.class)))
                .thenReturn(mockActiveCoupons);

        mockMvc.perform(get("/api/coupons/active")
                        .param("currentDateTime", currentDateTime.toString())
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].code").value("ACTIVE1"))
                .andExpect(jsonPath("$.content[0].status").value("UNUSED"))
                .andExpect(jsonPath("$.content[0].issuedAt").exists())
                .andExpect(jsonPath("$.content[0].expiredAt").exists())
                .andExpect(jsonPath("$.content[0].policyId").value(10L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].code").value("ACTIVE2"))
                .andExpect(jsonPath("$.content[1].status").value("UNUSED"))
                .andExpect(jsonPath("$.content[1].issuedAt").exists())
                .andExpect(jsonPath("$.content[1].expiredAt").exists())
                .andExpect(jsonPath("$.content[1].policyId").value(20L));
    }

    @DisplayName("특정 쿠폰정책에 속한 쿠폰 조회")
    @Test
    void getCouponsByPolicies() throws Exception {
        Long policyId = 100L;
        int page = 0;
        int pageSize = 10;

        Mockito.when(couponService.getCouponsByPolicy(any(CouponFindCouponPolicyIdRequestDto.class)))
                .thenReturn(mockActiveCoupons);

        mockMvc.perform(get("/api/coupons/policies/{policy-id}", policyId)
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].code").value("ACTIVE1"))
                .andExpect(jsonPath("$.content[0].status").value("UNUSED"))
                .andExpect(jsonPath("$.content[0].issuedAt").exists())
                .andExpect(jsonPath("$.content[0].expiredAt").exists())
                .andExpect(jsonPath("$.content[0].policyId").value(10L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].code").value("ACTIVE2"))
                .andExpect(jsonPath("$.content[1].status").value("UNUSED"))
                .andExpect(jsonPath("$.content[1].issuedAt").exists())
                .andExpect(jsonPath("$.content[1].expiredAt").exists())
                .andExpect(jsonPath("$.content[1].policyId").value(20L));
    }

    @DisplayName("쿠폰상태로 쿠폰검색")
    @Test
    void getCouponsByStatus() throws Exception {
        String status = "UNUSED";
        int page = 0;
        int pageSize = 5;

        List<CouponResponseDto> couponList = Arrays.asList(
                new CouponResponseDto(1L, "ACTIVE1", Status.UNUSED, LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5), 10L),
                new CouponResponseDto(2L, "ACTIVE2", Status.UNUSED, LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(10), 20L)
        );

        mockStatusCoupons = new PageImpl<>(couponList, PageRequest.of(page, pageSize), couponList.size());

        when(couponService.getCouponsByStatus(any(CouponFindStatusRequestDto.class)))
                .thenReturn(mockStatusCoupons);

        mockMvc.perform(get("/api/coupons/status")
                        .param("status", status)
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].code").value("ACTIVE1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].code").value("ACTIVE2"));
    }

    @DisplayName("기한이 만료된 쿠폰들의 상태를 EXPIRED 로 변경")
    @Test
    void updateExpiredCoupons() throws Exception {
        doNothing().when(couponService).updateExpiredCoupon(any(CouponUpdateExpiredRequestDto.class));
        when(couponService.getExpiredCoupons(any(CouponExpiredRequestDto.class))).thenReturn(mockResponse);

        mockMvc.perform(patch("/api/coupons/expired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].code").value("CODE123"))
                .andExpect(jsonPath("$.content[0].status").value("EXPIRED"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].code").value("CODE456"))
                .andExpect(jsonPath("$.content[1].status").value("EXPIRED"));

        // Verify interactions with the service layer
        verify(couponService, times(1)).updateExpiredCoupon(any(CouponUpdateExpiredRequestDto.class));
        verify(couponService, times(1)).getExpiredCoupons(any(CouponExpiredRequestDto.class));
    }

    @Disabled
    @DisplayName("쿠폰 상태가 변경되었습니다")
    @Test
    void useCoupon() throws Exception {
        Long couponId = 1L;
        Long mcMemberId = 1L;
        MemberCouponUseRequestDto memberCouponUseRequestDto = new MemberCouponUseRequestDto(mcMemberId, couponId);

        mockMvc.perform(patch("/api/coupons/{coupon-id}/use", couponId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("mcMemberId", String.valueOf(mcMemberId)))
                .andExpect(status().isOk())
                .andExpect(content().string("쿠폰 상태가 변경되었습니다"));

        verify(couponService, times(1)).useCoupon(couponId);
    }
}