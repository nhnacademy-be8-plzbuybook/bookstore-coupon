package com.nhnacademy.bookstorecoupon.dto.couponpolicy;

import jakarta.validation.constraints.NotNull;

public record CouponPolicyNameRequestDto(
        @NotNull
        String name // 쿠폰정책 이름
) {
}
