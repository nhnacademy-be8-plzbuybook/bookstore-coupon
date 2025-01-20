package com.nhnacademy.boostorecoupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorecoupon.dto.coupon.*;
import com.nhnacademy.boostorecoupon.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorecoupon.entity.Coupon;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import com.nhnacademy.boostorecoupon.enums.SaleType;
import com.nhnacademy.boostorecoupon.enums.Status;
import com.nhnacademy.boostorecoupon.service.CouponService;
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

@Disabled
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
                CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockPolicy)
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
                new CouponResponseDto(1L, "CODE1", Status.EXPIRED, now.minusDays(10), now.minusDays(5), CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockPolicy)),
                new CouponResponseDto(2L, "CODE2", Status.EXPIRED, now.minusDays(20), now.minusDays(15), CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockPolicy))
        );
        mockExpiredCoupons = new PageImpl<>(couponResponseDtoList, PageRequest.of(0, 10), couponResponseDtoList.size());

        List<CouponResponseDto> couponList = Arrays.asList(
                new CouponResponseDto(1L, "ACTIVE1", Status.UNUSED, LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5), CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockPolicy)),
                new CouponResponseDto(2L, "ACTIVE2", Status.UNUSED, LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(10), CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockPolicy))
        );
        mockActiveCoupons = new PageImpl<>(couponList, PageRequest.of(0, 10), couponList.size());

        requestDto = new CouponUpdateExpiredRequestDto(
                LocalDateTime.of(2023, 12, 31, 23, 59, 59), // expiredDate
                "UNUSED",
                0,
                10
        );

        List<CouponResponseDto> expiredCouponList = Arrays.asList(
                new CouponResponseDto(1L, "CODE123", Status.EXPIRED, LocalDateTime.now().minusDays(10), LocalDateTime.now(), CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockPolicy)),
                new CouponResponseDto(2L, "CODE456", Status.EXPIRED, LocalDateTime.now().minusDays(20), LocalDateTime.now(), CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockPolicy))
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
                new CouponResponseDto(1L, "ACTIVE1", Status.UNUSED, LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5), CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockPolicy)),
                new CouponResponseDto(2L, "ACTIVE2", Status.UNUSED, LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(10), CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockPolicy))
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


    @DisplayName("쿠폰 상태가 변경되었습니다")
    @Test
    void useCoupon() throws Exception {
        Long couponId = 1L;
        Long mcMemberId = 1L;

        mockMvc.perform(patch("/api/coupons/{coupon-id}/use", couponId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("mcMemberId", String.valueOf(mcMemberId)))
                .andExpect(status().isOk())
                .andExpect(content().string("쿠폰 상태가 변경되었습니다"));

        verify(couponService, times(1)).useCoupon(couponId);
    }
}