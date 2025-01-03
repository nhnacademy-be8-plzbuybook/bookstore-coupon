package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.couponpolicy.*;
import org.springframework.data.domain.Page;

public interface CouponPolicyService {

    CouponPolicyResponseDto createCouponPolicy(CouponPolicySaveRequestDto requestDto); // 쿠폰정책 생성

    CouponPolicyResponseDto findByName(CouponPolicyNameRequestDto couponPolicyNameRequestDto); // 쿠폰정책 이름으로 쿠폰정책 조회

    CouponPolicyResponseDto findById(CouponPolicyIdRequestDto couponPolicyIdRequestDto); // 쿠폰정책 ID 로 조회 기능

    Page<CouponPolicyResponseDto> findActiveCouponPolicy(CouponPolicyActiveRequestDto requestDto); // 활성화된 쿠폰 정책 목록 조회

    void addTargetToPolicy(CouponTargetAddRequestDto ctTargetAddRequestDto); // 쿠폰대상 연결 추가
}

