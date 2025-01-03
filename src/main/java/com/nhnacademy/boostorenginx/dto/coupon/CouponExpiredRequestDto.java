package com.nhnacademy.boostorenginx.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CouponExpiredRequestDto(
        @NotNull
        LocalDateTime expiredAt, // 쿠폰 만료일자
        @Min(0)
        int page, // 페이지 값
        @Min(1)
        int pageSize // 페이지 사이즈
) {
}
