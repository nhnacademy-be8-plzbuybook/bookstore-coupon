package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;

import java.util.List;

public interface CouponPolicyService {
    Long createCouponPolicy(CouponPolicySaveRequestDto requestDto, List<Long> targetIdList);
}
