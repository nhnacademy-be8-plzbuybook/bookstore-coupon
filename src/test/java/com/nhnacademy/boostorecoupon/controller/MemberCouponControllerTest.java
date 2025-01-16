package com.nhnacademy.boostorecoupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorecoupon.dto.membercoupon.*;
import com.nhnacademy.boostorecoupon.enums.Status;
import com.nhnacademy.boostorecoupon.service.MemberCouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        MemberCouponCreateRequestDto requestDto = new MemberCouponCreateRequestDto(1L, 100L);
        MemberCouponResponseDto responseDto = new MemberCouponResponseDto(
                1L,
                1L,
                new MemberCouponResponseDto.CouponResponseDto(
                        100L,
                        "ABC123",
                        Status.UNUSED,
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().plusDays(10),
                        1L,
                        "할인쿠폰",
                        "RATIO",
                        BigDecimal.valueOf(10000),
                        BigDecimal.valueOf(5000),
                        10,
                        true,
                        "BOOK",
                        true
                )
        );

        when(memberCouponService.createMemberCoupon(any(MemberCouponCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/member-coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.coupon.couponId").value(100L))
                .andExpect(jsonPath("$.coupon.name").value("할인쿠폰"))
                .andExpect(jsonPath("$.coupon.status").value("UNUSED"));

        verify(memberCouponService, times(1)).createMemberCoupon(any(MemberCouponCreateRequestDto.class));
    }

    @DisplayName("회원이 쿠폰을 사용")
    @Test
    void useMemberCoupon() throws Exception {
        Long memberId = 1L;
        Long couponId = 100L;
        MemberCouponUseRequestDto requestDto = new MemberCouponUseRequestDto(memberId, couponId);

        doNothing().when(memberCouponService).useMemberCoupon(any(MemberCouponUseRequestDto.class));

        mockMvc.perform(patch("/api/member-coupons/members/{member-id}/coupons/{coupon-id}/use", memberId, couponId)
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

        MemberCouponGetResponseDto mockCoupon = new MemberCouponGetResponseDto(
                1L,
                "TESTCODE",
                "UNUSED",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(10),
                "Test Policy",
                "RATIO",
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(5000),
                10,
                true,
                "BOOK"
        );

        Page<MemberCouponGetResponseDto> mockPage = new PageImpl<>(
                List.of(mockCoupon), pageable, 1
        );

        when(memberCouponService.getMemberCouponsByMemberId(any(MemberCouponFindByMemberIdRequestDto.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/member-coupons/member/{member-id}", memberId)
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code").value("TESTCODE"))
                .andExpect(jsonPath("$.content[0].status").value("UNUSED"))
                .andExpect(jsonPath("$.content[0].name").value("Test Policy"))
                .andExpect(jsonPath("$.content[0].saleType").value("RATIO"))
                .andExpect(jsonPath("$.content[0].minimumAmount").value(10000))
                .andExpect(jsonPath("$.content[0].discountLimit").value(5000))
                .andExpect(jsonPath("$.content[0].discountRatio").value(10))
                .andExpect(jsonPath("$.content[0].couponScope").value("BOOK"));

        verify(memberCouponService, times(1)).getMemberCouponsByMemberId(any(MemberCouponFindByMemberIdRequestDto.class));
    }

    @DisplayName("쿠폰 ID 로 회원쿠폰 조회")
    @Test
    void getMemberCouponsByCouponId() throws Exception {
        Long couponId = 100L;
        int page = 0;
        int pageSize = 10;

        List<MemberCouponResponseDto> mockMemberCouponList = List.of(
                new MemberCouponResponseDto(
                        1L,
                        couponId,
                        new MemberCouponResponseDto.CouponResponseDto(
                                couponId,
                                "COUPON_CODE",
                                Status.UNUSED,
                                LocalDateTime.now().minusDays(1),
                                LocalDateTime.now().plusDays(10),
                                100L,
                                "Test Policy",
                                "AMOUNT",
                                BigDecimal.valueOf(10000),
                                BigDecimal.valueOf(5000),
                                0,
                                true,
                                "BOOK",
                                true
                        )
                )
        );

        Page<MemberCouponResponseDto> mockPage = new PageImpl<>(mockMemberCouponList, PageRequest.of(page, pageSize), mockMemberCouponList.size());


        when(memberCouponService.getMemberCouponsByCouponId(any(MemberCouponFindByCouponIdRequestDto.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/member-coupons/coupons/{coupon-id}", couponId)
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].coupon.couponId").value(couponId))
                .andExpect(jsonPath("$.content[0].coupon.code").value("COUPON_CODE"))
                .andExpect(jsonPath("$.content[0].coupon.status").value("UNUSED"))
                .andExpect(jsonPath("$.content[0].coupon.couponPolicyId").value(100L))
                .andExpect(jsonPath("$.content[0].coupon.name").value("Test Policy"));

        verify(memberCouponService, times(1)).getMemberCouponsByCouponId(any(MemberCouponFindByCouponIdRequestDto.class));
    }

    @DisplayName("회원이 사용 가능한 쿠폰 목록 조회 (UNUSED 상태의 쿠폰)")
    @Test
    void getUnusedMemberCouponsByMemberId() throws Exception {
        Long memberId = 1L;
        int page = 0;
        int pageSize = 10;

        MemberCouponGetResponseDto mockCoupon = new MemberCouponGetResponseDto(
                1L,
                "TESTCODE",
                "UNUSED",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(10),
                "Test Policy",
                "RATIO",
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(5000),
                10,
                true,
                "BOOK"
        );

        Page<MemberCouponGetResponseDto> mockPage = new PageImpl<>(
                List.of(mockCoupon), PageRequest.of(page, pageSize), 1
        );

        when(memberCouponService.getUnusedMemberCouponsByMemberId(any(MemberCouponFindByMemberIdRequestDto.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/member-coupons/member/{member-id}/unused", memberId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code").value("TESTCODE"))
                .andExpect(jsonPath("$.content[0].status").value("UNUSED"))
                .andExpect(jsonPath("$.content[0].name").value("Test Policy"))
                .andExpect(jsonPath("$.content[0].saleType").value("RATIO"))
                .andExpect(jsonPath("$.content[0].minimumAmount").value(10000))
                .andExpect(jsonPath("$.content[0].discountLimit").value(5000))
                .andExpect(jsonPath("$.content[0].discountRatio").value(10))
                .andExpect(jsonPath("$.content[0].couponScope").value("BOOK"));

        verify(memberCouponService, times(1)).getUnusedMemberCouponsByMemberId(any(MemberCouponFindByMemberIdRequestDto.class));
    }
}