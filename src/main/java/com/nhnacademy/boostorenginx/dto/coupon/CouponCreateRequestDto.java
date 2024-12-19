package com.nhnacademy.boostorenginx.dto.coupon;

import java.time.LocalDateTime;

public record CouponCreateRequestDto(
        Long couponPolicyId,
        LocalDateTime expiredAt
) {
}