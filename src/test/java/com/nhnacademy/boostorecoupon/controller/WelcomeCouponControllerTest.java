package com.nhnacademy.boostorecoupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorecoupon.dto.welcome.WelcomeCouponRequestDto;
import com.nhnacademy.boostorecoupon.service.WelcomeCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WelcomeCouponController.class)
class WelcomeCouponControllerTest {

    @MockBean
    private WelcomeCouponService welcomeCouponService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private WelcomeCouponRequestDto requestDto;


    @BeforeEach
    void setUp() {
        Long memberId = 1L;
        LocalDateTime registeredAt = LocalDateTime.of(2025, 1, 1, 12, 0);
        requestDto = new WelcomeCouponRequestDto(memberId, registeredAt);
    }

    @Test
    void issueWelcomeCoupon() throws Exception {
        doNothing().when(welcomeCouponService).issueWelcomeCoupon(Mockito.any(WelcomeCouponRequestDto.class));

        mockMvc.perform(post("/api/coupons/welcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("웰컴 쿠폰이 성공적으로 발급되었습니다"));

        Mockito.verify(welcomeCouponService, Mockito.times(1)).issueWelcomeCoupon(Mockito.any(WelcomeCouponRequestDto.class));
    }
}