package com.nhnacademy.boostorenginx.dto.coupontarget;

import lombok.Getter;

// 쿠폰정책 요청
@Getter
public class CouponTargetRequestDto {
    private Long policyId;
    private Long targetId;
}
