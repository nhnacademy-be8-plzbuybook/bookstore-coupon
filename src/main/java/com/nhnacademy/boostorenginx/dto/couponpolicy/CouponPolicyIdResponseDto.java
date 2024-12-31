package com.nhnacademy.boostorenginx.dto.couponpolicy;

import com.nhnacademy.boostorenginx.enums.SaleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponPolicyIdResponseDto(
        Long couponPolicyId,
        String couponPolicyName,
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
}
