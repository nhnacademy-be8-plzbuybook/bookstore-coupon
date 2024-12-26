package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;

public interface CouponTargetService {

    CouponTargetResponseDto createCouponTarget(CouponTargetAddRequestDto dto); // 쿠폰대상 생성
}
