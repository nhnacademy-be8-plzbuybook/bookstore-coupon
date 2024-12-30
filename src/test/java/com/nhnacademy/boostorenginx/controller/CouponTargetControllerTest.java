package com.nhnacademy.boostorenginx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.error.CouponTargetException;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
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

    @DisplayName("쿠폰 대상 생성")
    @Test
    void createCouponTarget() throws Exception {
        CouponTargetAddRequestDto requestDto = new CouponTargetAddRequestDto(1L, 100L);
        CouponTargetResponseDto responseDto = new CouponTargetResponseDto(1L, 1L, 100L);

        Mockito.when(couponTargetService.createCouponTarget(any(CouponTargetAddRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/coupons/targets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.couponTargetId()))
                .andExpect(jsonPath("$.policyId").value(responseDto.couponPolicyId()))
                .andExpect(jsonPath("$.targetId").value(responseDto.ctTargetId()));
    }

    @DisplayName("쿠폰 대상 생성 - 서비스계층에서 예외가 발생할 경우")
    @Test
    void createCouponTarget_ServiceException() throws Exception {
        CouponTargetAddRequestDto requestDto = new CouponTargetAddRequestDto(1L, 100L);

        Mockito.doThrow(new CouponTargetException("이미 등록된 쿠폰 대상입니다"))
                .when(couponTargetService).createCouponTarget(any(CouponTargetAddRequestDto.class));

        mockMvc.perform(post("/api/coupons/targets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("이미 등록된 쿠폰 대상입니다"));
    }
}