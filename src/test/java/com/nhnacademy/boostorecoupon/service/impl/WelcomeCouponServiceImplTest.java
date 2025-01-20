package com.nhnacademy.boostorecoupon.service.impl;

import com.nhnacademy.boostorecoupon.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponTargetAddRequestDto;
import com.nhnacademy.boostorecoupon.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorecoupon.dto.welcome.WelcomeCouponRequestDto;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import com.nhnacademy.boostorecoupon.enums.SaleType;
import com.nhnacademy.boostorecoupon.enums.Status;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WelcomeCouponServiceImplTest {

    @InjectMocks
    private WelcomeCouponServiceImpl welcomeCouponServiceImpl;

    @Mock
    private CouponPolicyService couponPolicyService;

    @Mock
    private CouponService couponService;

    @Mock
    private MemberCouponService memberCouponService;

    @Mock
    private CouponPolicy couponPolicy;

    private WelcomeCouponRequestDto welcomeCouponRequestDto;

    @BeforeEach
    void setUp() {
        welcomeCouponRequestDto = new WelcomeCouponRequestDto(1L, LocalDateTime.of(2025, 1, 1, 10, 0));
        couponPolicy = new CouponPolicy(
                "WELCOME_COUPON",
                SaleType.AMOUNT,
                new BigDecimal("50000"),
                new BigDecimal("10000"),
                0,
                true,
                "ALL",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                true
        );
    }

    @DisplayName("Welcome 쿠폰발급")
    @Test
    void issueWelcomeCoupon() {
        CouponPolicyResponseDto mockPolicyResponse = new CouponPolicyResponseDto(
                1L, "WELCOME_COUPON", SaleType.AMOUNT.toString(), new BigDecimal("50000"),
                new BigDecimal("10000"), 0, true, "ALL",
                LocalDateTime.now(), LocalDateTime.now().plusDays(30), true
        );

        CouponResponseDto mockCouponResponse = new CouponResponseDto(
                1L,
                "WELCOME123",
                Status.UNUSED,
                LocalDateTime.now(),
                LocalDateTime.of(2025, 1, 31, 23, 59, 59),
                CouponResponseDto.CouponPolicyDto.fromCouponPolicy(couponPolicy)
        );

        when(couponPolicyService.createCouponPolicy(any(CouponPolicySaveRequestDto.class))).thenReturn(mockPolicyResponse);
        when(couponPolicyService.addTargetToPolicy(any(CouponTargetAddRequestDto.class))).thenReturn(null);
        when(couponService.createCoupon(any(CouponCreateRequestDto.class))).thenReturn(mockCouponResponse);
        when(memberCouponService.createMemberCoupon(any(MemberCouponCreateRequestDto.class))).thenReturn(null);

        assertDoesNotThrow(() -> welcomeCouponServiceImpl.issueWelcomeCoupon(welcomeCouponRequestDto));

        verify(couponPolicyService, times(1)).createCouponPolicy(argThat(request -> {
            assertEquals("WELCOME_COUPON", request.name());
            assertEquals(new BigDecimal("50000"), request.minimumAmount());
            assertEquals(new BigDecimal("10000"), request.discountLimit());
            return true;
        }));

        verify(couponPolicyService, times(1)).addTargetToPolicy(argThat(request -> {
            assertEquals(mockPolicyResponse.id(), request.policyId());
            assertEquals(welcomeCouponRequestDto.memberId(), request.ctTargetId());
            return true;
        }));

        verify(couponService, times(1)).createCoupon(argThat(request -> {
            assertEquals(mockPolicyResponse.id(), request.couponPolicyId());
            assertEquals(LocalDateTime.of(2025, 1, 31, 23, 59, 59), request.expiredAt());
            return true;
        }));

        verify(memberCouponService, times(1)).createMemberCoupon(argThat(request -> {
            assertEquals(mockCouponResponse.id(), request.couponId());
            assertEquals(welcomeCouponRequestDto.memberId(), request.mcMemberId());
            return true;
        }));
    }
}