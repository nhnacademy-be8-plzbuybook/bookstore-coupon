package com.nhnacademy.boostorenginx.dto.couponpolicy;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponPolicyNameResponseDto(
        Long couponPolicyId,
        String couponPolicyName,
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
    public static CouponPolicyNameResponseDto fromEntity(CouponPolicy couponPolicy) {
        return new CouponPolicyNameResponseDto(
                couponPolicy.getId(),
                couponPolicy.getName(),
                couponPolicy.getSaleType().toString(),
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