package com.nhnacademy.boostorenginx.dto.couponpolicy;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record CouponPolicyResponseDto(
        Long id,
        String name,
        SaleType saleType,
        BigDecimal minimumAmount,
        BigDecimal discountLimit,
        Integer discountRatio,
        boolean isStackable,
        String couponScope,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean couponActive
) {
    public static CouponPolicyResponseDto fromCouponPolicy(CouponPolicy couponPolicy) {
        return new CouponPolicyResponseDto(
                couponPolicy.getId(),
                couponPolicy.getName(),
                couponPolicy.getSaleType(),
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