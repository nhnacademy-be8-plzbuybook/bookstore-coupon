package com.nhnacademy.boostorenginx.dto.coupon;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponCodeResponseDto(
        Long id,
        String code,
        Status status,
        LocalDateTime issuedAt,
        LocalDateTime expiredAt,
        CouponPolicyDto couponPolicy
) {
    public static CouponCodeResponseDto fromCoupon(Coupon coupon) {
        return new CouponCodeResponseDto(
                coupon.getId(),
                coupon.getCode(),
                coupon.getStatus(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt(),
                CouponPolicyDto.fromCouponPolicy(coupon.getCouponPolicy()) // Mapping CouponPolicy
        );
    }

    public record CouponPolicyDto(
            Long id,
            String name,
            String saleType,
            BigDecimal minimumAmount,
            BigDecimal discountLimit,
            Integer discountRatio,
            boolean isStackable,
            String couponScope,
            LocalDateTime startDate,
            LocalDateTime endDate,
            boolean couponActive
    ) {
        public static CouponPolicyDto fromCouponPolicy(CouponPolicy couponPolicy) {
            return new CouponPolicyDto(
                    couponPolicy.getId(),
                    couponPolicy.getName(),
                    couponPolicy.getSaleType().name(),
                    couponPolicy.getMinimumAmount(),
                    couponPolicy.getDiscountLimit(),
                    couponPolicy.getDiscountRatio(),
                    couponPolicy.isStackable(),
                    couponPolicy.getCouponScope(),
                    couponPolicy.getStartDate(),
                    couponPolicy.getEndDate(),
                    couponPolicy.isCouponActive()
            );
        }
    }
}