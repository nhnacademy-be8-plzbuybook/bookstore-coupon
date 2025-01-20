package com.nhnacademy.boostorecoupon.service.impl;

import com.nhnacademy.boostorecoupon.dto.birthday.BirthdayCouponRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponTargetAddRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorecoupon.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorecoupon.service.CouponPolicyService;
import com.nhnacademy.boostorecoupon.service.CouponService;
import com.nhnacademy.boostorecoupon.service.MemberCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BirthdayCouponServiceImplTest {

    @InjectMocks
    private BirthdayCouponServiceImpl birthdayCouponService;

    @Mock
    private CouponPolicyService couponPolicyService;
    @Mock
    private MemberCouponService memberCouponService;
    @Mock
    private CouponService couponService;

    private final LocalDateTime now = LocalDateTime.now();

    private BirthdayCouponRequestDto birthdayCouponRequestDto;
    private CouponPolicyResponseDto couponPolicyResponseDto;
    private CouponTargetResponseDto couponTargetResponseDto;
    private CouponResponseDto couponResponseDto;

    @BeforeEach
    void setUp() {
        birthdayCouponRequestDto = new BirthdayCouponRequestDto(1L, now);
        couponPolicyResponseDto = new CouponPolicyResponseDto(
                100L,
                "BIRTHDAY_COUPON",
                "AMOUNT",
                new BigDecimal("10000"),
                new BigDecimal("5000"),
                0,
                true,
                "ALL",
                now,
                now.plusDays(30),
                true
        );
        couponTargetResponseDto = new CouponTargetResponseDto(200L, 100L, 1L);
        couponResponseDto = new CouponResponseDto(
                300L,
                "COUPON_CODE",
                null,
                now,
                now.plusDays(30),
                null
        );
    }

    @DisplayName("생일 쿠폰 발급")
    @Test
    void issueBirthdayCoupon() {
        when(couponPolicyService.createCouponPolicy(any(CouponPolicySaveRequestDto.class))).thenReturn(couponPolicyResponseDto);
        when(couponPolicyService.addTargetToPolicy(any(CouponTargetAddRequestDto.class))).thenReturn(couponTargetResponseDto);
        when(couponService.createCoupon(any(CouponCreateRequestDto.class))).thenReturn(couponResponseDto);

        birthdayCouponService.issueBirthdayCoupon(birthdayCouponRequestDto);

        verify(couponPolicyService, times(1)).createCouponPolicy(any(CouponPolicySaveRequestDto.class));
        verify(couponPolicyService, times(1)).addTargetToPolicy(any(CouponTargetAddRequestDto.class));
        verify(couponService, times(1)).createCoupon(any(CouponCreateRequestDto.class));
        verify(memberCouponService, times(1)).createMemberCoupon(any(MemberCouponCreateRequestDto.class));
    }
}