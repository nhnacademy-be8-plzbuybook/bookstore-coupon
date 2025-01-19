package com.nhnacademy.boostorecoupon.service;

import com.nhnacademy.boostorecoupon.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.boostorecoupon.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.boostorecoupon.dto.calculation.ValidationCouponCalculation;

public interface CouponCalculationService {

    CouponCalculationResponseDto applyOrderProductCoupon(Long couponId, CouponCalculationRequestDto couponCalculationRequestDto); // 회원이 쿠폰을 사용할때 가격 계산

    ValidationCouponCalculation validateCouponCalculation(Long couponId, CouponCalculationRequestDto couponCalculationRequestDto);
}
