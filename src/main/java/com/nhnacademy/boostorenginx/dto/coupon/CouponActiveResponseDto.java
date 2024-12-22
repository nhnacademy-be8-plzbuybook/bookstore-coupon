package com.nhnacademy.boostorenginx.dto.coupon;

import com.nhnacademy.boostorenginx.entity.Coupon;

import java.time.LocalDateTime;

public record CouponActiveResponseDto(
        Long couponId,
        String code,
        String status,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt
) {
    public static CouponActiveResponseDto fromEntity(Coupon coupon) {
        return new CouponActiveResponseDto(
                coupon.getId(),
                coupon.getCode(),
                coupon.getStatus().toString(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt()
        );
    }
}
