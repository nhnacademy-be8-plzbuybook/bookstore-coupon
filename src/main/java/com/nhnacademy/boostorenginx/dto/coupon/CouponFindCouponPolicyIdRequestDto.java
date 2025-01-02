package com.nhnacademy.boostorenginx.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponFindCouponPolicyIdRequestDto(
        @Min(0)
        @NotNull
        Long policyId, // 쿠폰정책 ID
        int page,
        int pageSize
) {
}
