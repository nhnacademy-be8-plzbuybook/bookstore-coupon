package com.nhnacademy.boostorenginx.dto.coupontarget;

import jakarta.validation.constraints.NotNull;

public record CouponTargetAddRequestDto(
        @NotNull
        Long policyId, // 쿠폰정책 ID
        @NotNull
        Long ctTargetId // 쿠폰대상 ID
) {
}
