package com.nhnacademy.bookstorecoupon.dto.couponpolicy;

import com.nhnacademy.bookstorecoupon.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.enums.SaleType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record CouponPolicySaveRequestDto(
        @NotNull String name,
        @NotNull SaleType saleType,
        @DecimalMin(value = "0.0", inclusive = false) BigDecimal minimumAmount,
        @DecimalMin(value = "0.0") BigDecimal discountLimit,
        @Min(0) @Max(100) Integer discountRatio,
        boolean isStackable,
        @NotNull String couponScope,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate,
        boolean couponActive
) {
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
                .couponActive(couponActive)
                .build();
    }
}