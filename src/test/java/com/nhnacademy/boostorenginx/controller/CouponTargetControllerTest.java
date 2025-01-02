package com.nhnacademy.boostorenginx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
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

    private CouponTargetAddRequestDto addRequestDto;
    private CouponTargetResponseDto responseDto;
    private CouponTargetGetRequestDto getRequestDto;
    private CouponTargetGetResponseDto getResponseDto;

    private Page<CouponTargetGetResponseDto> getResponseDtos;

    @BeforeEach
    void setUp() {
        getResponseDto = new CouponTargetGetResponseDto(
                1L,
                100L,
                1L,
                "CATEGORY"
        );
        getResponseDtos = new PageImpl<>(List.of(getResponseDto));
    }

    @DisplayName("쿠폰 대상 생성")
    @Test
    void createCouponTarget() throws Exception {
        addRequestDto = new CouponTargetAddRequestDto(1L, 100L);
        responseDto = new CouponTargetResponseDto(1L, 1L, 100L);

        when(couponTargetService.createCouponTarget(any(CouponTargetAddRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/coupons/targets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.couponTargetId").value(responseDto.couponTargetId()))
                .andExpect(jsonPath("$.couponPolicyId").value(responseDto.couponPolicyId()))
                .andExpect(jsonPath("$.ctTargetId").value(responseDto.ctTargetId()));
    }

    @DisplayName("쿠폰 대상 생성 - 서비스계층에서 예외가 발생할 경우")
    @Test
    void createCouponTarget_ServiceException() throws Exception {
        addRequestDto = new CouponTargetAddRequestDto(1L, 100L);

        Mockito.doThrow(new CouponTargetException("이미 등록된 쿠폰 대상입니다"))
                .when(couponTargetService).createCouponTarget(any(CouponTargetAddRequestDto.class));

        mockMvc.perform(post("/api/coupons/targets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("이미 등록된 쿠폰 대상입니다"));
    }

    @DisplayName("특정 쿠폰정책에 속하는 쿠폰대상 목록 조회")
    @Test
    void getCouponTarget() throws Exception {
        getRequestDto = new CouponTargetGetRequestDto(1L, 0, 10);

        when(couponTargetService.getCouponTargetsByPolicyId(any(CouponTargetGetRequestDto.class)))
                .thenReturn(getResponseDtos);

        mockMvc.perform(get("/api/coupons/targets/policy")
                        .param("couponPolicyId", "1")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].couponTargetId").value(getResponseDto.couponTargetId()))
                .andExpect(jsonPath("$.content[0].ctTargetId").value(getResponseDto.ctTargetId()))
                .andExpect(jsonPath("$.content[0].couponPolicyId").value(getResponseDto.couponPolicyId()))
                .andExpect(jsonPath("$.content[0].scope").value(getResponseDto.scope()));

        verify(couponTargetService, times(1)).getCouponTargetsByPolicyId(any(CouponTargetGetRequestDto.class));
    }

    @DisplayName("특정 쿠폰정책에 속하는 쿠폰대상 목록 조회 - 요청 Dto 가 잘못된 경우")
    @Test
    void getCouponTarget_badRequest() throws Exception {
        mockMvc.perform(get("/api/coupons/targets/policy")
                        .param("couponPolicyId", "1")
                        .param("page", "-1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/coupons/targets/policy")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/coupons/targets/policy")
                        .param("couponPolicyId", "1")
                        .param("page", "0")
                        .param("pageSize", "-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}