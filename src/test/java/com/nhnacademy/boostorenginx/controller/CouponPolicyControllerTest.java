package com.nhnacademy.boostorenginx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorenginx.dto.couponpolicy.*;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest {

    @MockBean
    private CouponPolicyService couponPolicyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private CouponPolicySaveRequestDto saveRequestDto;
    private CouponPolicyResponseDto responseDto;
    private CouponPolicyResponseDto mockResponseDto;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        saveRequestDto = new CouponPolicySaveRequestDto(
                "test-policy",
                SaleType.RATIO,
                new BigDecimal("1000"),
                new BigDecimal("5000"),
                10,
                true,
                "CATEGORY",
                now.minusDays(1),
                now.plusDays(1),
                true
        );

        responseDto = new CouponPolicyResponseDto(
                1L,
                "test-policy",
                SaleType.RATIO,
                new BigDecimal("1000"),
                new BigDecimal("5000"),
                10,
                true,
                "CATEGORY",
                now.minusDays(1),
                now.plusDays(1),
                true
        );

        mockResponseDto = new CouponPolicyResponseDto(
                1L,
                "test-policy",
                SaleType.RATIO,
                new BigDecimal("1000"),
                new BigDecimal("5000"),
                10,
                true,
                "CATEGORY",
                now.minusDays(1),
                now.plusDays(1),
                true
        );
    }

    @DisplayName("쿠폰정책 생성 테스트")
    @Test
    void createCouponPolicy() throws Exception {
        CouponPolicySaveResponseDto expectedResponse = new CouponPolicySaveResponseDto(responseDto.id());

        Mockito.when(couponPolicyService.createCouponPolicy(any(CouponPolicySaveRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/coupon-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.couponPolicyId").value(expectedResponse.couponPolicyId()));

        Mockito.verify(couponPolicyService, Mockito.times(1))
                .createCouponPolicy(any(CouponPolicySaveRequestDto.class));
    }

    @DisplayName("쿠폰정책 이름으로 검색")
    @Test
    void findByName() throws Exception {
        Mockito.when(couponPolicyService.findByName(any(CouponPolicyNameRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/coupon-policies/name/{name}", "test-policy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.name").value(responseDto.name()))
                .andExpect(jsonPath("$.saleType").value(responseDto.saleType().toString()))
                .andExpect(jsonPath("$.minimumAmount").value(responseDto.minimumAmount().toString()))
                .andExpect(jsonPath("$.discountLimit").value(responseDto.discountLimit().toString()))
                .andExpect(jsonPath("$.discountRatio").value(responseDto.discountRatio()))
                .andExpect(jsonPath("$.isStackable").value(responseDto.isStackable()))
                .andExpect(jsonPath("$.couponScope").value(responseDto.couponScope()))
                .andExpect(jsonPath("$.startDate").value(responseDto.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(responseDto.endDate().toString()))
                .andExpect(jsonPath("$.couponActive").value(responseDto.couponActive()));

        Mockito.verify(couponPolicyService, Mockito.times(1))
                .findByName(any(CouponPolicyNameRequestDto.class));
    }

    @Test
    void findById() throws Exception {
        Mockito.when(couponPolicyService.findById(any(CouponPolicyIdRequestDto.class)))
                .thenReturn(mockResponseDto);

        mockMvc.perform(get("/api/coupon-policies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockResponseDto.id()))
                .andExpect(jsonPath("$.name").value(mockResponseDto.name()))
                .andExpect(jsonPath("$.saleType").value(mockResponseDto.saleType().toString()))
                .andExpect(jsonPath("$.minimumAmount").value(mockResponseDto.minimumAmount().toString()))
                .andExpect(jsonPath("$.discountLimit").value(mockResponseDto.discountLimit().toString()))
                .andExpect(jsonPath("$.discountRatio").value(mockResponseDto.discountRatio()))
                .andExpect(jsonPath("$.isStackable").value(mockResponseDto.isStackable()))
                .andExpect(jsonPath("$.couponScope").value(mockResponseDto.couponScope()))
                .andExpect(jsonPath("$.startDate").value(mockResponseDto.startDate().toString()))
                .andExpect(jsonPath("$.endDate").value(mockResponseDto.endDate().toString()))
                .andExpect(jsonPath("$.couponActive").value(mockResponseDto.couponActive()));

        Mockito.verify(couponPolicyService, Mockito.times(1))
                .findById(eq(new CouponPolicyIdRequestDto(1L)));
    }

    @DisplayName("활성화된 쿠폰정책 조회")
    @Test
    void findActiveCouponPolicies() throws Exception {
        Page<CouponPolicyResponseDto> mockPage = new PageImpl<>(List.of(mockResponseDto));
        Mockito.when(couponPolicyService.findActiveCouponPolicy(eq(true), any(Pageable.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/coupon-policies/active")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(mockResponseDto.id()))
                .andExpect(jsonPath("$.content[0].name").value(mockResponseDto.name()))
                .andExpect(jsonPath("$.content[0].saleType").value(mockResponseDto.saleType().toString()))
                .andExpect(jsonPath("$.content[0].minimumAmount").value(mockResponseDto.minimumAmount().toString()))
                .andExpect(jsonPath("$.content[0].discountLimit").value(mockResponseDto.discountLimit().toString()))
                .andExpect(jsonPath("$.content[0].discountRatio").value(mockResponseDto.discountRatio()))
                .andExpect(jsonPath("$.content[0].isStackable").value(mockResponseDto.isStackable()))
                .andExpect(jsonPath("$.content[0].couponScope").value(mockResponseDto.couponScope()))
                .andExpect(jsonPath("$.content[0].startDate").value(mockResponseDto.startDate().toString()))
                .andExpect(jsonPath("$.content[0].endDate").value(mockResponseDto.endDate().toString()))
                .andExpect(jsonPath("$.content[0].couponActive").value(mockResponseDto.couponActive()));

        Mockito.verify(couponPolicyService, Mockito.times(1))
                .findActiveCouponPolicy(eq(true), any(Pageable.class));
    }

    @DisplayName("쿠폰정책에 쿠폰대상 추가")
    @Test
    void addCouponTargets() throws Exception {
        CouponTargetAddRequestDto addRequest = new CouponTargetAddRequestDto(1L, 10L);
        Mockito.doNothing().when(couponPolicyService).addTargetToPolicy(any(CouponTargetAddRequestDto.class));
        mockMvc.perform(post("/api/coupon-policies/addTargets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andExpect(status().isCreated());
        Mockito.verify(couponPolicyService, Mockito.times(1))
                .addTargetToPolicy(any(CouponTargetAddRequestDto.class));
    }

}