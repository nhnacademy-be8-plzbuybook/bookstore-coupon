package com.nhnacademy.boostorecoupon.service.impl;

import com.nhnacademy.boostorecoupon.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorecoupon.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorecoupon.dto.refundcoupon.RefundCouponRequestDto;
import com.nhnacademy.boostorecoupon.entity.Coupon;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import com.nhnacademy.boostorecoupon.enums.SaleType;
import com.nhnacademy.boostorecoupon.enums.Status;
import com.nhnacademy.boostorecoupon.error.NotFoundCouponException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefundCouponServiceImplTest {

    @InjectMocks
    private RefundCouponServiceImpl refundCouponService;

    @Mock
    private CouponService couponService;

    @Mock
    private MemberCouponService memberCouponService;

    private final LocalDateTime now = LocalDateTime.now();
    private RefundCouponRequestDto refundCouponRequestDto;
    private Coupon mockCoupon;


    @BeforeEach
    void setUp() {
        refundCouponRequestDto = new RefundCouponRequestDto(1L, 100L);
        CouponPolicy couponPolicy = CouponPolicy.builder()
                .name("Test Policy")
                .saleType(SaleType.AMOUNT)
                .minimumAmount(new BigDecimal("5000"))
                .discountLimit(new BigDecimal("1000"))
                .discountRatio(0)
                .isStackable(false)
                .couponScope("CATEGORY")
                .startDate(now.minusDays(10))
                .endDate(now.plusDays(10))
                .couponActive(true)
                .build();

        mockCoupon = new Coupon(
                Status.UNUSED,
                now.minusDays(5),
                now.plusDays(5),
                couponPolicy
        );
    }

    @DisplayName("쿠폰 환불")
    @Test
    void refundCoupon() {
        when(couponService.existsById(refundCouponRequestDto.getCouponId())).thenReturn(true);
        when(couponService.getCouponById(refundCouponRequestDto.getCouponId())).thenReturn(mockCoupon);
        when(couponService.createCoupon(any(CouponCreateRequestDto.class))).thenReturn(new CouponResponseDto(
                2L, "NEW_COUPON_CODE", Status.UNUSED, now, now.plusDays(10),
                CouponResponseDto.CouponPolicyDto.fromCouponPolicy(mockCoupon.getCouponPolicy())
        ));

        refundCouponService.refundCoupon(refundCouponRequestDto);

        verify(couponService, times(1)).existsById(refundCouponRequestDto.getCouponId());
        verify(couponService, times(1)).getCouponById(refundCouponRequestDto.getCouponId());
        verify(couponService, times(1)).createCoupon(any(CouponCreateRequestDto.class));
        verify(memberCouponService, times(1)).createMemberCoupon(any(MemberCouponCreateRequestDto.class));
    }

    @DisplayName("쿠폰 ID 에 해당하는 쿠폰이 없는 경우")
    @Test
    void refundCoupon_NotFoundCouponException() {
        when(couponService.existsById(refundCouponRequestDto.getCouponId())).thenReturn(false);

        assertThrows(NotFoundCouponException.class,
                () -> refundCouponService.refundCoupon(refundCouponRequestDto));

        verify(couponService, times(1)).existsById(refundCouponRequestDto.getCouponId());
        verify(couponService, never()).getCouponById(anyLong());
        verify(couponService, never()).createCoupon(any(CouponCreateRequestDto.class));
        verify(memberCouponService, never()).createMemberCoupon(any(MemberCouponCreateRequestDto.class));
    }
}