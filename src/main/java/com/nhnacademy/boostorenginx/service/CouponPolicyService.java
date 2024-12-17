package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;

import java.util.List;

public interface CouponPolicyService {
    Long createCouponPolicy(CouponPolicySaveRequestDto requestDto);

    void addCouponTargetList(Long couponPolicyId, List<Long> targetIdList);

    CouponPolicy findByName(String name);
}
