package com.nhnacademy.boostorenginx.dto;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CouponPolicySaveRequestDto(String name, SaleType saleType, BigDecimal minimumAmount,
                                         BigDecimal discountLimit, Integer discountRatio, boolean isStackable,
                                         String couponScope
        , LocalDateTime startDate, LocalDateTime endDate, List<Long> targetIdList) {

    public CouponPolicy toEntity() {
        return CouponPolicy.builder()
                .name(name)
                .saleType(saleType)
                .minimumAmount(minimumAmount)
                .discountLimit(discountLimit)
                .discountRatio(discountRatio)
                .isStackable(isStackable)
                .couponScope(couponScope)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
