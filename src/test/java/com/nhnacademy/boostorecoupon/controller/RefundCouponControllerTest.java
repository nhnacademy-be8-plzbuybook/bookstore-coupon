package com.nhnacademy.boostorecoupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorecoupon.dto.refundcoupon.RefundCouponRequestDto;
import com.nhnacademy.boostorecoupon.service.RefundCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RefundCouponController.class)
class RefundCouponControllerTest {

    @MockBean
    private RefundCouponService refundCouponService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private RefundCouponRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new RefundCouponRequestDto(1L, 100L);
    }

    @DisplayName("쿠폰환불")
    @Test
    void refundCoupon() throws Exception {
        doNothing().when(refundCouponService).refundCoupon(Mockito.any(RefundCouponRequestDto.class));

        mockMvc.perform(post("/api/coupons/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("환불이 완료되었습니다"));

        Mockito.verify(refundCouponService, Mockito.times(1)).refundCoupon(Mockito.any(RefundCouponRequestDto.class));
    }

}