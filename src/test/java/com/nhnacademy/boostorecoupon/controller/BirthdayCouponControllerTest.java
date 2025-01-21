package com.nhnacademy.boostorecoupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorecoupon.dto.birthday.BirthdayCouponRequestDto;
import com.nhnacademy.boostorecoupon.service.BirthdayCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BirthdayCouponController.class)
class BirthdayCouponControllerTest {

    @MockBean
    private BirthdayCouponService birthdayCouponService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private BirthdayCouponRequestDto requestDto;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        Long memberId = 1L;
        LocalDateTime registerAt = now.minusDays(1);
        requestDto = new BirthdayCouponRequestDto(memberId, registerAt);
    }

    @DisplayName("생일쿠폰 발급")
    @Test
    void issueBirthdayCoupon() throws Exception {

        doNothing().when(birthdayCouponService).issueBirthdayCoupon(Mockito.any(BirthdayCouponRequestDto.class));

        mockMvc.perform(post("/api/coupons/birthday")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("생일 쿠폰이 성공적으로 발급되었습니다"));

        verify(birthdayCouponService).issueBirthdayCoupon(Mockito.any(BirthdayCouponRequestDto.class));
    }
}