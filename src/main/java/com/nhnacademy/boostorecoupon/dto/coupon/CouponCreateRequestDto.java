package com.nhnacademy.boostorecoupon.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CouponCreateRequestDto(
        @Min(0)
        @NotNull
        Long couponPolicyId, // 쿠폰정책 ID

        @NotNull
        LocalDateTime expiredAt // 쿠폰만료일
) {
}