package com.nhnacademy.boostorenginx.dto.coupon;

import java.time.LocalDateTime;

public record CouponUpdateExpiredRequestDto(
        LocalDateTime expiredDate,
        String status,
        int pageSize,
        int page
) {
}
