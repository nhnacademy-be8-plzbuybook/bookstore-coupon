package com.nhnacademy.boostorenginx.dto.membercoupon;

public record MemberCouponFindByCouponIdRequestDto(
        Long couponId,
        int page,
        int pageSize
) {
}
