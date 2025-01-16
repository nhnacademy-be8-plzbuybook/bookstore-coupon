package com.nhnacademy.boostorecoupon.dto.membercoupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MemberCouponFindByCouponIdRequestDto(
        @Min(0)
        @NotNull
        Long couponId, // 쿠폰 ID
        @Min(0)
        int page,
        @Min(1)
        int pageSize
) {
}
