package com.nhnacademy.boostorenginx.dto.coupontarget;

import org.springframework.data.domain.Pageable;

public record CouponTargetGetRequestDto(
        Long couponPolicyId, // 쿠폰정책 ID
        int page,
        int pageSize
) {
}
