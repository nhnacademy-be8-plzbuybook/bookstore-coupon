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
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id")
    private CouponPolicy couponPolicy;

    @Column(name = "ct_target_id")
    private Long targetId;

    public CouponTarget(Long targetId) {
        this.targetId = targetId;
    }
}
