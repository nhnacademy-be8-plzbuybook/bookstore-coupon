package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import org.springframework.data.domain.Page;

public interface CouponTargetService {

    CouponTargetResponseDto createCouponTarget(CouponTargetAddRequestDto dto); // 쿠폰대상 생성

    Page<CouponTargetGetResponseDto> getCouponTargetsByPolicyId(CouponTargetGetRequestDto dto); // 쿠폰대상 목록 조회
}
