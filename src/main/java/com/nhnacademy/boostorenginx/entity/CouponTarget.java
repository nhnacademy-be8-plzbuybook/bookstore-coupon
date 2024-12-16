package com.nhnacademy.boostorenginx.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class CouponTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_target_id")
    private Long id; // 쿠폰대상 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id")
    private CouponPolicy couponPolicy; // 쿠폰정책 (외래키)

    @Column(name = "ct_target_id", unique = true)
    private Long targetId; // 쿠폰대상 -> 다른 테이블의 엔티티의 식별키 참조

    public CouponTarget(CouponPolicy couponPolicy, Long targetId) {
        this.couponPolicy = couponPolicy;
        this.targetId = targetId;
    }
}
