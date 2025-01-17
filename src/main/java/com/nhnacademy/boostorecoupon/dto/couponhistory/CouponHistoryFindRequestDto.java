package com.nhnacademy.boostorecoupon.dto.couponhistory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponHistoryFindRequestDto(
        @Min(0)
        @NotNull
        Long couponHistoryId, // 쿠폰변경이력
        @Min(0)
        int page,
        @Min(1)
        int pageSize
) {
}
