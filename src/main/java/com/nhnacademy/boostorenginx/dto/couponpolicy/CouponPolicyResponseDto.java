package com.nhnacademy.boostorenginx.dto.couponpolicy;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CouponPolicyResponseDto {
    private Long id;
    private String name;
    private String saleType; // SaleType 타입이 아닌 String 타입 주의
    private BigDecimal minimumAmount;
    private BigDecimal discountLimit;
    private Integer discountRatio;
    private boolean isStackable;
    private String couponScope;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean couponActive;

    // CouponPolicy 객체로 Dto 생성
    public CouponPolicyResponseDto(CouponPolicy couponPolicy) {
        this.id = couponPolicy.getId();
        this.name = couponPolicy.getName();
        this.saleType = couponPolicy.getSaleType().name();
        this.minimumAmount = couponPolicy.getMinimumAmount();
        this.discountLimit = couponPolicy.getDiscountLimit();
        this.discountRatio = couponPolicy.getDiscountRatio();
        this.isStackable = couponPolicy.isStackable();
        this.couponScope = couponPolicy.getCouponScope();
        this.startDate = couponPolicy.getStartDate();
        this.endDate = couponPolicy.getEndDate();
        this.couponActive = couponPolicy.isCouponActive();
    }
}
