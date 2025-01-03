package com.nhnacademy.boostorenginx.dto.couponpolicy;

import jakarta.validation.constraints.NotNull;

public record CouponPolicyNameRequestDto(
        @NotNull
        String name // 쿠폰정책 이름
) {
}
