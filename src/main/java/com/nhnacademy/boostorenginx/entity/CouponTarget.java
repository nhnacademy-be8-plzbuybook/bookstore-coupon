package com.nhnacademy.boostorenginx.entity;

import jakarta.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id")
    private CouponPolicy couponPolicy; // 쿠폰정책 ID

    @Column(name = "ct_target_id")
     private Long targetId; // 적용범위대상 ID

    public CouponTarget(Long targetId) {
        this.targetId = targetId;
    }
}
