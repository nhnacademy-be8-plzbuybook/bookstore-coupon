package com.nhnacademy.boostorenginx.dto.coupon;

import java.time.LocalDateTime;

public record CouponExpiredRequestDto(
        LocalDateTime expiredAt,
        int page,
        int pageSize
) {
}
