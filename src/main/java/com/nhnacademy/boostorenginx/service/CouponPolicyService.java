package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyNameRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;

import java.util.Optional;

public interface CouponPolicyService {

    Long createCouponPolicy(CouponPolicySaveRequestDto requestDto); // 쿠폰정책 생성 기능 -> 쿠폰정책 ID 반환

    Optional<CouponPolicy> findByName(CouponPolicyNameRequestDto requestDto); // 쿠폰정책 이름으로 조회 기능

    Optional<CouponPolicy> findById(Long couponPolicyId); // 쿠폰정책 번호로 조회 기능

    void addTargetToPolicy(CouponTargetAddRequestDto requestDto); // 쿠폰대상 연결 추가
}

