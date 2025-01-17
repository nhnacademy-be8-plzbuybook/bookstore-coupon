package com.nhnacademy.boostorecoupon.service;

import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetSearchRequestDto;
import org.springframework.data.domain.Page;

public interface CouponTargetService {

    CouponTargetResponseDto createCouponTarget(CouponTargetSaveRequestDto couponTargetSaveRequestDto); // 쿠폰대상 생성

    Page<CouponTargetGetResponseDto> getCouponTargetsByPolicyId(CouponTargetSearchRequestDto couponTargetSeearchReqeustDto); // 쿠폰대상 목록 조회
}
