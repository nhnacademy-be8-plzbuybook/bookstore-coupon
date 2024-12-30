package com.nhnacademy.boostorenginx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorenginx.dto.membercoupon.*;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.service.MemberCouponService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberCouponController.class)
class MemberCouponControllerTest {

    @MockBean
    private MemberCouponService memberCouponService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("회원에게 쿠폰발급")
    @Test
    void createMemberCoupon() throws Exception {
        MemberCouponCreateRequestDto requestDto = new MemberCouponCreateRequestDto(1L, 100L, 0, 10);
        MemberCouponResponseDto responseDto = new MemberCouponResponseDto(
                1L,
                1L,
                new MemberCouponResponseDto.CouponResponseDto(
                        100L,
                        "ABC123",
                        Status.UNUSED,
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().plusDays(10),
                        "Holiday Discount",
                        BigDecimal.valueOf(5000),
                        10
                )
        );

        Mockito.when(memberCouponService.createMemberCoupon(any(MemberCouponCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/coupons/member-coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberCouponId").value(responseDto.memberCouponId()))
                .andExpect(jsonPath("$.memberId").value(responseDto.memberId()))
                .andExpect(jsonPath("$.coupon.id").value(responseDto.coupon().id()))
                .andExpect(jsonPath("$.coupon.code").value(responseDto.coupon().code()))
                .andExpect(jsonPath("$.coupon.status").value(responseDto.coupon().status().toString()));

        verify(memberCouponService, times(1)).createMemberCoupon(any(MemberCouponCreateRequestDto.class));
    }

    @DisplayName("회원이 쿠폰을 사용")
    @Test
    void useMemberCoupon() throws Exception {
        MemberCouponUseRequestDto requestDto = new MemberCouponUseRequestDto(1L, 100L);

        doNothing().when(memberCouponService).useMemberCoupon(any(MemberCouponUseRequestDto.class));

        mockMvc.perform(patch("/api/coupons/member-coupon/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("쿠폰이 성공적으로 사용되었습니다."));

        verify(memberCouponService, times(1)).useMemberCoupon(any(MemberCouponUseRequestDto.class));
    }

    @DisplayName("회원 ID 로 회원쿠폰조회")
    @Test
    void getMemberCouponsByMemberId() throws Exception {
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 5);

        MemberCouponResponseDto.CouponResponseDto mockCoupon = new MemberCouponResponseDto.CouponResponseDto(
                100L,
                "TESTCODE",
                Status.UNUSED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(10),
                "Test Policy",
                BigDecimal.valueOf(5000),
                10
        );

        Page<MemberCouponResponseDto> mockPage = new PageImpl<>(
                List.of(new MemberCouponResponseDto(1L, memberId, mockCoupon)), pageable, 1
        );

        when(memberCouponService.getMemberCouponsByMemberId(any(MemberCouponFindByMemberIdRequestDto.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/coupons/member-coupon/member/{memberId}", memberId)
                        .param("page", "0")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].memberCouponId").value(1L))
                .andExpect(jsonPath("$.content[0].memberId").value(memberId));

        verify(memberCouponService, times(1)).getMemberCouponsByMemberId(any(MemberCouponFindByMemberIdRequestDto.class));
    }

    @DisplayName("쿠폰 ID 로 회원쿠폰 조회")
    @Test
    void getMemberCouponsByCouponId() throws Exception {
        Long couponId = 100L;
        Pageable pageable = PageRequest.of(0, 5);

        MemberCouponResponseDto.CouponResponseDto mockCoupon = new MemberCouponResponseDto.CouponResponseDto(
                couponId,
                "TESTCODE",
                Status.UNUSED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(10),
                "Test Policy",
                BigDecimal.valueOf(5000),
                10
        );

        Page<MemberCouponResponseDto> mockPage = new PageImpl<>(
                List.of(new MemberCouponResponseDto(1L, 1L, mockCoupon)), pageable, 1
        );

        when(memberCouponService.getMemberCouponsByCouponId(any(MemberCouponFindByCouponIdRequestDto.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/coupons/member-coupon/coupon/{couponId}", couponId)
                        .param("page", "0")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].memberCouponId").value(1L))
                .andExpect(jsonPath("$.content[0].coupon.id").value(couponId))
                .andExpect(jsonPath("$.content[0].coupon.code").value("TESTCODE"))
                .andExpect(jsonPath("$.content[0].coupon.status").value("UNUSED"));

        verify(memberCouponService, times(1)).getMemberCouponsByCouponId(any(MemberCouponFindByCouponIdRequestDto.class));
    }
}