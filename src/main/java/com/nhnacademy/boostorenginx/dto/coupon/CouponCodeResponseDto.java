package com.nhnacademy.boostorenginx.dto.coupon;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponCodeResponseDto(
        CouponInfo coupon,
        CouponPolicyInfo couponPolicy
) {
    public static CouponCodeResponseDto fromEntity(Coupon coupon) {
        CouponPolicy policy = coupon.getCouponPolicy();
        return new CouponCodeResponseDto(
                new CouponInfo(
                        coupon.getId(),
                        coupon.getCode(),
                        coupon.getStatus().toString(),
                        coupon.getIssuedAt(),
                        coupon.getExpiredAt()
                ),
                new CouponPolicyInfo(
                        policy.getId(),
                        policy.getName(),
                        policy.getSaleType().toString(),
                        policy.getMinimumAmount(),
                        policy.getDiscountLimit(),
                        policy.getDiscountRatio(),
                        policy.isStackable(),
                        policy.getCouponScope(),
                        policy.getStartDate(),
                        policy.getEndDate(),
                        policy.isCouponActive()
                )
        );
    }

    public record CouponInfo(
            Long couponId,
            String code,
            String status,
            LocalDateTime issuedAt,
            LocalDateTime expiredAt
    ) {
    }

    public record CouponPolicyInfo(
            Long couponPolicyId,
            String name,
            String saleType,
            BigDecimal minimumAmount,
            BigDecimal discountLimit,
            Integer discountRatio,
            boolean isStackable,
            String scope,
            LocalDateTime startDate,
            LocalDateTime endDate,
            boolean active
    ) {
    }
}