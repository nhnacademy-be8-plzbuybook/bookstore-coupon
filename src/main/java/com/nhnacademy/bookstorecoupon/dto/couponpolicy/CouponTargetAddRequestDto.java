package com.nhnacademy.bookstorecoupon.dto.couponpolicy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponTargetAddRequestDto(
        @NotNull
        @Min(0)
        Long policyId, // 쿠폰정책 ID
        @NotNull
        @Min(0)
        Long ctTargetId // 쿠폰대상 ID
) {
}
