package com.nhnacademy.boostorenginx.dto.couponpolicy;

public record CouponPolicyActiveRequestDto(
        boolean couponActive, // 쿠폰정책활성화 여부
        int page,
        int pageSize
) {
}
