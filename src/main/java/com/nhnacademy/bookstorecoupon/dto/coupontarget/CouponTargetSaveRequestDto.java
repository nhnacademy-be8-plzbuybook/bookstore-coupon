package com.nhnacademy.bookstorecoupon.dto.coupontarget;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponTargetSaveRequestDto(
        @NotNull
        @Min(0)
        Long couponPolicyId, // 쿠폰대상 ID
        @NotNull
        @Min(0)
        Long ctTargetId // 참조하는 대상의 ID
) {
}
