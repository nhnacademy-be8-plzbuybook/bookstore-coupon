package com.nhnacademy.boostorenginx.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponPolicyService couponPolicyService;

    @Autowired
    private ObjectMapper objectMapper;

    private CouponPolicySaveRequestDto requestDto;
    private List<CouponTarget> couponTargetList;

    @BeforeEach
    void setUp() {
        requestDto = new CouponPolicySaveRequestDto(
                "test",
                SaleType.RATIO,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(10000),
                10,
                false,
                "CATEGORY",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                true
        );
    }

    @Test
    void createCouponPolicyTest() throws Exception {
        long policyId = 1L;
        Mockito.when(couponPolicyService.createCouponPolicy(any())).thenReturn(policyId);
        mockMvc.perform(post("/api/coupon-policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void addCouponTargetsTest() throws Exception {
        long policyId = 1L;
        List<Long> targetIdList = List.of(1L, 2L);

        mockMvc.perform(post("/api/coupon-policies/{policyId}/targets", policyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(targetIdList)))
                .andExpect(status().isCreated());
    }
}
