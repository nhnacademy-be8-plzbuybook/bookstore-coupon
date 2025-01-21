package com.nhnacademy.bookstorecoupon.service;

import com.nhnacademy.bookstorecoupon.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.bookstorecoupon.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.bookstorecoupon.dto.calculation.ValidationCouponCalculation;
import com.nhnacademy.bookstorecoupon.dto.calculation.ValidationCouponCalculationRequestDto;

public interface CouponCalculationService {

    CouponCalculationResponseDto applyOrderProductCoupon(Long couponId, CouponCalculationRequestDto couponCalculationRequestDto); // 회원이 쿠폰을 사용할때 가격 계산

    ValidationCouponCalculation validateCouponCalculation(Long couponId, ValidationCouponCalculationRequestDto validationCouponCalculationRequestDto);
}
