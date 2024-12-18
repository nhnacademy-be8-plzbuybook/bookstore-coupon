package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.CouponPolicyNameRequestDto;
import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;


public interface CouponPolicyService {

    Long createCouponPolicy(CouponPolicySaveRequestDto requestDto); // 쿠폰정책 생성 기능

    CouponPolicy findByName(CouponPolicyNameRequestDto requestDto); // 쿠폰정책 이름으로 조회 기능

    CouponPolicy findById(Long couponPolicyId); // 쿠폰정책 번호로 조회 기능
}
