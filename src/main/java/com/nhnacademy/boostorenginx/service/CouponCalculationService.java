package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationResponseDto;

public interface CouponCalculationService {

    CouponCalculationResponseDto applyOrderProductCoupon(Long memberId, Long couponId, CouponCalculationRequestDto couponCalculationRequestDto); // 회원이 쿠폰을 사용할때 가격 계산

}
