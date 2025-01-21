package com.nhnacademy.bookstorecoupon.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CouponUpdateExpiredRequestDto(
        @NotNull
        LocalDateTime expiredDate, // 쿠폰 만료일
        @NotNull
        String status, // 쿠폰 상태
        @Min(0)
        int page, // 페이지 값
        @Min(1)
        int size // 페이지 크기
) {
}
