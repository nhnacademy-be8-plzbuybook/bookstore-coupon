package com.nhnacademy.boostorenginx.dto.coupon;

import java.time.LocalDateTime;

public record CouponCodeResponseDto(
        String code,
        String status, // 문자열 타입 주의
        LocalDateTime issuedAt,
        LocalDateTime expiredAt,
        Long couponPolicyId // CouponPolicy
) {
}
