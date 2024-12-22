package com.nhnacademy.boostorenginx.dto.coupon;

import com.nhnacademy.boostorenginx.entity.Coupon;

import java.time.LocalDateTime;

public record CouponFindCouponPolicyIdResponseDto(
        Long couponId,
        String code,
        String status,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt
) {
    public static CouponFindCouponPolicyIdResponseDto fromEntity(Coupon coupon) {
        return new CouponFindCouponPolicyIdResponseDto(
                coupon.getId(),
                coupon.getCode(),
                coupon.getStatus().toString(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt()
        );
    }
}
