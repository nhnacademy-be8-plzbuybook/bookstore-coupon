package com.nhnacademy.boostorecoupon.service;

import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetSearchRequestDto;
import org.springframework.data.domain.Page;

public interface CouponTargetService {

    Page<CouponTargetGetResponseDto> getCouponTargetsByPolicyId(CouponTargetSearchRequestDto couponTargetSearchRequestDto); // 쿠폰정책 ID 로 쿠폰대상 조회

}
