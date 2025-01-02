package com.nhnacademy.boostorenginx.dto.couponpolicy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponPolicyIdRequestDto(
        @NotNull
        @Min(0)
        Long couponPolicyId // 쿠폰정책 ID
) {
}
