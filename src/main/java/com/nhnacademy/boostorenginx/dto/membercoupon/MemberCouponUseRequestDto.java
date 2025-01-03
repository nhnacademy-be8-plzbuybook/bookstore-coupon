package com.nhnacademy.boostorenginx.dto.membercoupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MemberCouponUseRequestDto(
        @Min(0)
        @NotNull
        Long memberId, // 회원 ID

        @Min(0)
        @NotNull
        Long couponId // 쿠폰 ID
) {
}
