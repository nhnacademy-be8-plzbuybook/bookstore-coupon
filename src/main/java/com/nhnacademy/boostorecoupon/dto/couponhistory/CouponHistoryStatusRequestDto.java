package com.nhnacademy.boostorecoupon.dto.couponhistory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponHistoryStatusRequestDto(
        @NotNull
        String status, // 쿠폰상태
        @Min(0)
        int page,
        @Min(1)
        int pageSize
) {
}
