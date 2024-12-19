package com.nhnacademy.boostorenginx.dto.coupon;

import java.time.LocalDateTime;

public record CouponUseRequestDto(
        Long couponId,
        LocalDateTime useTime
) {
}
