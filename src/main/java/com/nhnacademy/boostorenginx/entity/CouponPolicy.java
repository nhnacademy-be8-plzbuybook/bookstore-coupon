package com.nhnacademy.boostorenginx.entity;

import com.nhnacademy.boostorenginx.enums.SaleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class CouponPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment 사용
    @Column(name = "coupon_policy_id")
    private Long id; // 쿠폰정책 ID

    @Column(nullable = false)
    private String name; // 쿠폰이름

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SaleType saleType; // 할인타입

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal minimumAmount; // 쿠폰적용최소금액

    @DecimalMin(value = "0.0")
    private BigDecimal discountLimit; // 최대할인금액

    @Min(value = 0)
    @Max(value = 100)
    private Integer discountRatio; // 할인비율

    private boolean isStackable; // 중복사용여부 (여러 쿠폰 간 중복적용여부)

    @Column(nullable = false)
    private String couponScope; // 쿠폰적용범위

    @Column(nullable = false)
    private LocalDateTime startDate; // 쿠폰사용시작일

    @Column(nullable = false)
    private LocalDateTime endDate; // 쿠폰사용종료일

    private boolean couponActive; // 쿠폰정책활성화 여부

    @OneToMany(mappedBy = "couponPolicy", cascade = CascadeType.ALL)
    private List<CouponTarget> couponTargetList = new ArrayList<>();

    @OneToMany(mappedBy = "couponPolicy") // @OneToMany 는 빈 리스트로 초기화됨
    private List<Coupon> couponList = new ArrayList<>();

    @Builder
    public CouponPolicy(String name, SaleType saleType, BigDecimal minimumAmount, BigDecimal discountLimit, Integer discountRatio, boolean isStackable, String couponScope, LocalDateTime startDate, LocalDateTime endDate, boolean couponActive) {
        this.name = name;
        this.saleType = saleType;
        this.minimumAmount = minimumAmount;
        this.discountLimit = discountLimit;
        this.discountRatio = discountRatio;
        this.isStackable = isStackable;
        this.couponScope = couponScope;
        this.startDate = startDate;
        this.endDate = endDate;
        this.couponActive = couponActive;
    }

    public void addCouponTarget(CouponTarget couponTarget) {
        couponTarget.setCouponPolicy(this);
        this.couponTargetList.add(couponTarget);
    }
}
