package com.nhnacademy.boostorenginx.dto.couponpolicy;

import jakarta.validation.constraints.NotNull;

public record CouponPolicyNameRequestDto(
        @NotNull
        String couponPolicyName // 쿠폰정책 이름
) {
}
