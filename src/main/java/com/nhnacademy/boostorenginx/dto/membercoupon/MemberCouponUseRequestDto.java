package com.nhnacademy.boostorenginx.dto.membercoupon;

public record MemberCouponUseRequestDto(
        Long couponId,
        Long memberId
) {
}
