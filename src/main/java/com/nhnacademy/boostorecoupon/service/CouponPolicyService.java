package com.nhnacademy.boostorecoupon.service;

import com.nhnacademy.boostorecoupon.dto.couponpolicy.*;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CouponPolicyService {

    CouponPolicyResponseDto createCouponPolicy(CouponPolicySaveRequestDto requestDto); // 쿠폰정책 생성

    Page<CouponPolicy> findAllCouponPolicies(Pageable pageable); // 쿠폰정책 전체 조회

    void findExpiredCouponPolicies(); // 기한이 만료되었지만 활성중인 쿠폰정책 목록 조회

    CouponPolicyResponseDto findByName(CouponPolicyNameRequestDto couponPolicyNameRequestDto); // 쿠폰정책 이름으로 쿠폰정책 조회

    CouponPolicyResponseDto findById(CouponPolicyIdRequestDto couponPolicyIdRequestDto); // 쿠폰정책 ID 로 조회 기능

    Page<CouponPolicyResponseDto> findActiveCouponPolicy(CouponPolicyActiveRequestDto requestDto); // 활성화된 쿠폰 정책 목록 조회

    void addTargetToPolicy(CouponTargetAddRequestDto ctTargetAddRequestDto); // 쿠폰대상 연결 추가

    CouponPolicyResponseDto findCouponPolicyById(Long couponId);
}

