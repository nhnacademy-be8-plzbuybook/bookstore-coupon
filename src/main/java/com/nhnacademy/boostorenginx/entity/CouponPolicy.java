package com.nhnacademy.boostorenginx.entity;

import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.enums.Scopes;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class CouponPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_policy_id")
    private Long id;
    private String name;
    private SaleType saleType;
    private boolean isStackable;
    private BigDecimal minimumAmount;
    private BigDecimal threshold;
    private Scopes couponScope;
    private Integer ratioDiscount;
    private BigDecimal amountDiscount;

    @OneToMany(mappedBy = "couponPolicy", fetch = FetchType.EAGER)
    private List<CouponTarget> couponTargetList;

    @Builder
    public CouponPolicy(String name, SaleType saleType, boolean isStackable, BigDecimal minimumAmount, BigDecimal threshold, Scopes couponScope, Integer ratioDiscount, BigDecimal amountDiscount) {
        this.name = name;
        this.saleType = saleType;
        this.isStackable = isStackable;
        this.minimumAmount = minimumAmount;
        this.threshold = threshold;
        this.couponScope = couponScope;
        this.ratioDiscount = ratioDiscount;
        this.amountDiscount = amountDiscount;
        this.couponTargetList = new ArrayList<>();
    }

    public void addCouponTarget(CouponTarget couponTarget) {
        couponTarget.setCouponPolicy(this);
        this.couponTargetList.add(couponTarget);
    }
}
