package com.nhnacademy.boostorenginx.dto.coupon;

import com.nhnacademy.boostorenginx.entity.Coupon;

import java.time.LocalDateTime;

public record CouponFindStatusResponseDto(
        Long couponId,
        String code,
        String status,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt
) {
    public static CouponFindStatusResponseDto fromEntity(Coupon coupon) {
        return new CouponFindStatusResponseDto(
                coupon.getId(),
                coupon.getCode(),
                coupon.getStatus().toString(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt()
        );
    }
}
