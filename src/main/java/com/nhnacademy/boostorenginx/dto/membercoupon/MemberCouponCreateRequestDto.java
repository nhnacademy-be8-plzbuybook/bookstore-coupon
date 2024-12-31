package com.nhnacademy.boostorenginx.dto.membercoupon;

public record MemberCouponCreateRequestDto(
        Long memberId,
        Long couponId,
        int page,
        int pageSize
) {
}
