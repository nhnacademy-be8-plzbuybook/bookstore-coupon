package com.nhnacademy.bookstorecoupon.service;

import com.nhnacademy.bookstorecoupon.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.bookstorecoupon.dto.coupontarget.CouponTargetSearchRequestDto;
import org.springframework.data.domain.Page;

public interface CouponTargetService {

    Page<CouponTargetGetResponseDto> getCouponTargetsByPolicyId(CouponTargetSearchRequestDto couponTargetSearchRequestDto); // 쿠폰정책 ID 로 쿠폰대상 조회

}
