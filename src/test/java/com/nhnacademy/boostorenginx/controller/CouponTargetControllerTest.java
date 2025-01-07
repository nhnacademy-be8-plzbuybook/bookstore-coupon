package com.nhnacademy.boostorenginx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSearchRequestDto;
import com.nhnacademy.boostorenginx.error.CouponTargetException;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CouponTargetController.class)
class CouponTargetControllerTest {

    @MockBean
    private CouponTargetService couponTargetService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CouponTargetSaveRequestDto saveRequestDto;
    private CouponTargetResponseDto targetResponseDto;

    @DisplayName("쿠폰 대상 생성")
    @Test
    void createCouponTarget() throws Exception {
        saveRequestDto = new CouponTargetSaveRequestDto(1L, 100L);
        targetResponseDto = new CouponTargetResponseDto(1L, 1L, 100L);

        when(couponTargetService.createCouponTarget(any(CouponTargetSaveRequestDto.class)))
                .thenReturn(targetResponseDto);

        mockMvc.perform(post("/api/coupon-targets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.couponTargetId").value(targetResponseDto.couponTargetId()))
                .andExpect(jsonPath("$.couponPolicyId").value(targetResponseDto.couponPolicyId()))
                .andExpect(jsonPath("$.ctTargetId").value(targetResponseDto.ctTargetId()));

        Mockito.verify(couponTargetService, Mockito.times(1))
                .createCouponTarget(any(CouponTargetSaveRequestDto.class));
    }

    @DisplayName("쿠폰 대상 생성 - 서비스계층에서 예외가 발생할 경우")
    @Test
    void createCouponTarget_ServiceException() throws Exception {
        saveRequestDto = new CouponTargetSaveRequestDto(1L, 100L);

        Mockito.doThrow(new CouponTargetException("이미 등록된 쿠폰 대상입니다"))
                .when(couponTargetService).createCouponTarget(any(CouponTargetSaveRequestDto.class));

        mockMvc.perform(post("/api/coupon-targets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("이미 등록된 쿠폰 대상입니다"));
    }

    @DisplayName("특정 쿠폰정책에 속하는 쿠폰대상 목록 조회")
    @Test
    void getCouponTarget() throws Exception {
        Long policyId = 1L;
        int page = 0;
        int pageSize = 10;

        CouponTargetGetResponseDto responseDto1 = new CouponTargetGetResponseDto(1L, 101L, policyId, "CATEGORY");
        CouponTargetGetResponseDto responseDto2 = new CouponTargetGetResponseDto(2L, 102L, policyId, "CATEGORY");
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CouponTargetGetResponseDto> mockPage = new PageImpl<>(List.of(responseDto1, responseDto2), pageable, 2
        );

        when(couponTargetService.getCouponTargetsByPolicyId(any(CouponTargetSearchRequestDto.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/coupon-targets")
                        .param("policy-id", String.valueOf(policyId))
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].couponTargetId").value(responseDto1.couponTargetId()))
                .andExpect(jsonPath("$.content[0].ctTargetId").value(responseDto1.ctTargetId()))
                .andExpect(jsonPath("$.content[0].couponPolicyId").value(responseDto1.couponPolicyId()))
                .andExpect(jsonPath("$.content[0].scope").value(responseDto1.scope()))
                .andExpect(jsonPath("$.content[1].couponTargetId").value(responseDto2.couponTargetId()))
                .andExpect(jsonPath("$.content[1].ctTargetId").value(responseDto2.ctTargetId()))
                .andExpect(jsonPath("$.content[1].couponPolicyId").value(responseDto2.couponPolicyId()))
                .andExpect(jsonPath("$.content[1].scope").value(responseDto2.scope()));

        Mockito.verify(couponTargetService, Mockito.times(1))
                .getCouponTargetsByPolicyId(any(CouponTargetSearchRequestDto.class));
    }

}