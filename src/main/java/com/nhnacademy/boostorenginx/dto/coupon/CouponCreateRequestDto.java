package com.nhnacademy.boostorenginx.dto.coupon;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CouponCreateRequestDto(
        @NotNull
        Long couponPolicyId,
        @NotNull
        LocalDateTime expiredAt
) {
}