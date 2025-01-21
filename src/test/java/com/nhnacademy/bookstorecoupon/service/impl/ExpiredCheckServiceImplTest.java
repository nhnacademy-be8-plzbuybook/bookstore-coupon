package com.nhnacademy.bookstorecoupon.service.impl;

import com.nhnacademy.bookstorecoupon.dto.coupon.CouponFindCouponPolicyIdRequestDto;
import com.nhnacademy.bookstorecoupon.dto.coupon.CouponUpdateExpiredRequestDto;
import com.nhnacademy.bookstorecoupon.entity.Coupon;
import com.nhnacademy.bookstorecoupon.enums.Status;
import com.nhnacademy.bookstorecoupon.service.CouponPolicyService;
import com.nhnacademy.bookstorecoupon.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ExpiredCheckServiceImplTest {

    @InjectMocks
    private ExpiredCheckServiceImpl expiredCheckService;

    @Mock
    private CouponPolicyService couponPolicyService;

    @Mock
    private CouponService couponService;

    @DisplayName("만료된 쿠폰 처리")
    @Test
    void checkExpiredCoupon() {
        Long policyId = 1L;
        List<Long> ids = List.of(policyId);
        Coupon coupon = Mockito.mock(Coupon.class);

        Page<Coupon> mockCouponsPage = new PageImpl<>(List.of(coupon));

        when(couponPolicyService.findExpiredCouponPolicies()).thenReturn(ids);
        when(couponService.getCouponsByPolicyId(any(CouponFindCouponPolicyIdRequestDto.class)))
                .thenReturn(mockCouponsPage);

        expiredCheckService.checkExpiredCoupon();

        verify(couponPolicyService, times(1)).findExpiredCouponPolicies();
        verify(couponService, times(1)).getCouponsByPolicyId(any(CouponFindCouponPolicyIdRequestDto.class));
        verify(couponService, times(1)).updateExpiredCoupon(any(CouponUpdateExpiredRequestDto.class));
        verify(coupon, times(1)).changeStatus(eq(Status.EXPIRED), any(LocalDateTime.class), eq("EXPIRED"));
    }

    @DisplayName("만료된 쿠폰 정책이 없는 경우")
    @Test
    void checkExpiredCoupon_NotExpiredCoupon() {
        when(couponPolicyService.findExpiredCouponPolicies()).thenReturn(Collections.emptyList());

        expiredCheckService.checkExpiredCoupon();

        verify(couponPolicyService, times(1)).findExpiredCouponPolicies();
        verify(couponService, never()).getCouponsByPolicyId(any(CouponFindCouponPolicyIdRequestDto.class));
        verify(couponService, never()).updateExpiredCoupon(any(CouponUpdateExpiredRequestDto.class));
    }

}