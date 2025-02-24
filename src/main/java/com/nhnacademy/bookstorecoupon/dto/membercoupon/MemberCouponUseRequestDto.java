package com.nhnacademy.bookstorecoupon.dto.membercoupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MemberCouponUseRequestDto(
        @Min(0)
        @NotNull
        Long mcMemberId, // 회원 ID

        @Min(0)
        @NotNull
        Long couponId // 쿠폰 ID
) {
}
