package com.nhnacademy.boostorenginx.dto.couponhistory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CouponHistoryDuringRequestDto(
        @NotNull
        LocalDateTime start, // 시작일
        @NotNull
        LocalDateTime end, // 종료일
        @Min(0)
        int page,
        @Min(1)
        int pageSize
) {
}
