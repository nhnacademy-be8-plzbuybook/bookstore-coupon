package com.nhnacademy.bookstorecoupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstorecoupon.dto.coupon.*;
import com.nhnacademy.bookstorecoupon.enums.Status;
import com.nhnacademy.bookstorecoupon.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.any;
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

    private CouponResponseDto mockCouponResponse;
    private Page<CouponResponseDto> mockCouponsPage;

    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        mockCouponResponse = new CouponResponseDto(
                1L,
                "CODE123",
                Status.UNUSED,
                now,
                now.plusDays(10),
                null
        );

        mockCouponsPage = new PageImpl<>(List.of(mockCouponResponse), PageRequest.of(0, 10), 1);
    }

    @DisplayName("쿠폰생성 기능")
    @Test
    void createCoupon() throws Exception {
        CouponCreateRequestDto createRequest = new CouponCreateRequestDto(1L, now.plusDays(10));
        CouponCreateResponseDto expectedResponse = new CouponCreateResponseDto(1L);

        Mockito.when(couponService.createCoupon(any(CouponCreateRequestDto.class))).thenReturn(mockCouponResponse);

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.couponId").value(expectedResponse.couponId()));

        Mockito.verify(couponService, Mockito.times(1)).createCoupon(any(CouponCreateRequestDto.class));
    }

    @DisplayName("모든 쿠폰 조회")
    @Test
    void getAllCoupons() throws Exception {
        Mockito.when(couponService.getAllCoupons(any(PageRequest.class))).thenReturn(mockCouponsPage);

        mockMvc.perform(get("/api/coupons")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].code").value("CODE123"));

        Mockito.verify(couponService, Mockito.times(1)).getAllCoupons(any(PageRequest.class));
    }

    @DisplayName("쿠폰 ID로 쿠폰 조회")
    @Test
    void getCouponById() throws Exception {
        Mockito.when(couponService.findCouponById(1L)).thenReturn(mockCouponResponse);

        mockMvc.perform(get("/api/coupons/id/{coupon-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("CODE123"));

        Mockito.verify(couponService, Mockito.times(1)).findCouponById(1L);
    }

    @DisplayName("특정 쿠폰정책에 속한 쿠폰 조회")
    @Test
    void getCouponsByPolicies() throws Exception {
        Mockito.when(couponService.getCouponsByPolicy(any(CouponFindCouponPolicyIdRequestDto.class)))
                .thenReturn(mockCouponsPage);

        mockMvc.perform(get("/api/coupons/policies/{policy-id}", 1L)
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].code").value("CODE123"));

        Mockito.verify(couponService, Mockito.times(1)).getCouponsByPolicy(any(CouponFindCouponPolicyIdRequestDto.class));
    }

    @DisplayName("쿠폰상태로 쿠폰검색")
    @Test
    void getCouponsByStatus() throws Exception {
        Mockito.when(couponService.getCouponsByStatus(any(CouponFindStatusRequestDto.class)))
                .thenReturn(mockCouponsPage);

        mockMvc.perform(get("/api/coupons/status")
                        .param("status", "UNUSED")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].code").value("CODE123"));

        Mockito.verify(couponService, Mockito.times(1)).getCouponsByStatus(any(CouponFindStatusRequestDto.class));
    }

    @DisplayName("쿠폰사용")
    @Test
    void useCoupon() throws Exception {
        Mockito.doNothing().when(couponService).useCoupon(1L);

        mockMvc.perform(patch("/api/coupons/{coupon-id}/use", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("쿠폰 상태가 변경되었습니다"));

        Mockito.verify(couponService, Mockito.times(1)).useCoupon(1L);
    }
}