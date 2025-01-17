package com.nhnacademy.boostorecoupon.dto.coupon;

import com.nhnacademy.boostorecoupon.entity.Coupon;
import com.nhnacademy.boostorecoupon.enums.Status;

import java.time.LocalDateTime;

public record CouponResponseDto(
        Long id,
        String code,
        Status status,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt,
        Long policyId
) {
    public static CouponResponseDto fromCoupon(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getId(),
                coupon.getCode(),
                coupon.getStatus(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt(),
                coupon.getCouponPolicy().getId()
        );
    }
}
