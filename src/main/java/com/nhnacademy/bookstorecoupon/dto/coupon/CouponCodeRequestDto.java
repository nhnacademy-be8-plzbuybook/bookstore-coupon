package com.nhnacademy.bookstorecoupon.dto.coupon;

import jakarta.validation.constraints.NotNull;

public record CouponCodeRequestDto(
        @NotNull
        String code // 쿠폰코드
) {
}
