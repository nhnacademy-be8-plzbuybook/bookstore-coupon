package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyIdRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyNameRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponPolicyService {

    CouponPolicyResponseDto createCouponPolicy(CouponPolicySaveRequestDto requestDto); // 쿠폰정책 생성

    CouponPolicyResponseDto findByName(CouponPolicyNameRequestDto requestDto); // 쿠폰정책 이름으로 쿠폰정책 조회

    CouponPolicyResponseDto findById(CouponPolicyIdRequestDto requestDto); // 쿠폰정책 ID 로 조회 기능

    Page<CouponPolicyResponseDto> findActiveCouponPolicy(boolean couponActive,Pageable pageable); // 활성화된 쿠폰 정책 목록 조회

    void addTargetToPolicy(CouponTargetAddRequestDto requestDto); // 쿠폰대상 연결 추가
}

