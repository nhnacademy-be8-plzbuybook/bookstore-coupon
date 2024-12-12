package com.nhnacademy.boostorenginx.dto;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.enums.Scopes;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

public record CouponPolicySaveRequestDto(String name, SaleType saleType, boolean isStackable, BigDecimal minimumAmount,
                                         BigDecimal threshold, BigDecimal amountDiscount, Integer ratioDiscount,
                                         Scopes scope, List<Long> targetIdList) {

    public CouponPolicy toEntity() {
        return CouponPolicy.builder()
                .name(name)
                .saleType(saleType)
                .isStackable(isStackable)
                .minimumAmount(minimumAmount)
                .threshold(threshold)
                .amountDiscount(amountDiscount)
                .ratioDiscount(ratioDiscount)
                .couponScope(scope)
                .build();
    }
}
