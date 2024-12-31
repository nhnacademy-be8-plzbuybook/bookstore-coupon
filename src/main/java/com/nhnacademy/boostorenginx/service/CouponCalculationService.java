package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationResponseDto;

public interface CouponCalculationService {

    CouponCalculationResponseDto applyOrderProductCoupon(CouponCalculationRequestDto couponCalculationRequestDto);

}
