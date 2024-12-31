package com.nhnacademy.boostorenginx.dto.couponhistory;

import java.time.LocalDateTime;

public record CouponHistoryDuringRequestDto(
        LocalDateTime start,
        LocalDateTime end,
        int page,
        int pageSize
) {
}
