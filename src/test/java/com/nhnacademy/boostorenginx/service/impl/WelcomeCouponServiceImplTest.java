package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.welcome.WelComeCouponRequestDto;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import com.nhnacademy.boostorenginx.service.CouponService;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
import com.nhnacademy.boostorenginx.service.MemberCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WelcomeCouponServiceImplTest {

    @InjectMocks
    private WelcomeCouponServiceImpl welcomeCouponServiceImpl;

    @Mock
    private CouponPolicyService couponPolicyService;

    @Mock
    private CouponTargetService couponTargetService;

    @Mock
    private MemberCouponService memberCouponService;

    @Mock
    private CouponService couponService;

    private WelComeCouponRequestDto welcomeCouponRequestDto;

    @BeforeEach
    void setUp() {
        welcomeCouponRequestDto = new WelComeCouponRequestDto(1L, LocalDateTime.now().plusDays(10));
    }

    @DisplayName("Welcome 쿠폰발급 테스트")
    @Test
    void issueWelcomeCoupon() {
        CouponPolicyResponseDto mockPolicyResponse = new CouponPolicyResponseDto(
                1L, "WELCOME_COUPON", SaleType.AMOUNT, new BigDecimal("50000"),
                new BigDecimal("10000"), 0, true, "ALL",
                LocalDateTime.now(), LocalDateTime.now().plusDays(30), true
        );
        CouponTargetResponseDto mockTargetResponse = new CouponTargetResponseDto(1L, 1L, 1L);
        CouponResponseDto mockCouponResponse = new CouponResponseDto(
                1L,
                "WELCOME123",
                Status.UNUSED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                1L
        );

        when(couponPolicyService.createCouponPolicy(any(CouponPolicySaveRequestDto.class))).thenReturn(mockPolicyResponse);
        when(couponTargetService.createCouponTarget(any(CouponTargetAddRequestDto.class))).thenReturn(mockTargetResponse);
        when(couponService.createCoupon(any(CouponCreateRequestDto.class))).thenReturn(mockCouponResponse);

        welcomeCouponServiceImpl.issueWelcomeCoupon(welcomeCouponRequestDto);

        verify(couponPolicyService).createCouponPolicy(argThat(request -> {
            assertEquals("WELCOME_COUPON", request.name());
            assertEquals(new BigDecimal("50000"), request.minimumAmount());
            assertEquals(new BigDecimal("10000"), request.discountLimit());
            return true;
        }));

        verify(couponTargetService).createCouponTarget(argThat(request -> {
            assertEquals(mockPolicyResponse.id(), request.policyId());
            assertEquals(welcomeCouponRequestDto.memberId(), request.ctTargetId());
            return true;
        }));

        verify(couponService).createCoupon(argThat(request -> {
            assertEquals(mockPolicyResponse.id(), request.couponPolicyId());
            assertEquals(welcomeCouponRequestDto.registeredAt().plusDays(30), request.expiredAt());
            return true;
        }));

        verify(memberCouponService).createMemberCoupon(argThat(request -> {
            assertEquals(mockCouponResponse.id(), request.couponId());
            assertEquals(welcomeCouponRequestDto.memberId(), request.memberId());
            return true;
        }));

        verify(couponPolicyService, times(1)).createCouponPolicy(any(CouponPolicySaveRequestDto.class));
        verify(couponTargetService, times(1)).createCouponTarget(any(CouponTargetAddRequestDto.class));
        verify(couponService, times(1)).createCoupon(any(CouponCreateRequestDto.class));
        verify(memberCouponService, times(1)).createMemberCoupon(any(MemberCouponCreateRequestDto.class));
    }
}