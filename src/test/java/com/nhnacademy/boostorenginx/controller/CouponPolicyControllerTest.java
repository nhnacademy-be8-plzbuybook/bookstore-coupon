package com.nhnacademy.boostorenginx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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