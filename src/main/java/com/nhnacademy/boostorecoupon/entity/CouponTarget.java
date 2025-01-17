package com.nhnacademy.boostorecoupon.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
public class CouponTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_target_id")
    private Long id; // 쿠폰대상 ID

    @Setter
    @ManyToOne
    @JoinColumn(name = "coupon_policy_id", nullable = false)
    private CouponPolicy couponPolicy; // 쿠폰정책 (N:1)

    @Setter
    @Column(name = "ct_target_id", nullable = false, unique = true)
    private Long ctTargetId; // 참조하는 대상의 ID

    @Builder
    public CouponTarget(CouponPolicy couponPolicy, Long ctTargetId) {
        this.couponPolicy = couponPolicy;
        this.ctTargetId = ctTargetId;
    }
}
