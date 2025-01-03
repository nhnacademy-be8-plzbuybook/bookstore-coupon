package com.nhnacademy.boostorenginx.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponFindStatusRequestDto(
        @NotNull
        String status, // 쿠폰 상태
        @Min(0)
        int page, // 페이지 값
        @Min(1)
        int pageSize // 페이지 사이즈
) {
}
