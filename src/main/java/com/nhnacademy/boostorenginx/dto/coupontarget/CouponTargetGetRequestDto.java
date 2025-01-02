package com.nhnacademy.boostorenginx.dto.coupontarget;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponTargetGetRequestDto(
        @NotNull
        Long couponPolicyId, // 쿠폰정책 ID
        @Min(0)
        int page,
        @Min(1)
        int pageSize
) {
}
