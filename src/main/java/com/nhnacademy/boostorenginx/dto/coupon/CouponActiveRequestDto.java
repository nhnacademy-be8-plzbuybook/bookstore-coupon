package com.nhnacademy.boostorenginx.dto.coupon;

import java.time.LocalDateTime;

public record CouponActiveRequestDto(
        LocalDateTime currentDateTime,
        int page,
        int pageSize
) {
}
