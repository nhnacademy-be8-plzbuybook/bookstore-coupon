package com.nhnacademy.bookstorecoupon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class MemberCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_coupon_id")
    private Long id; // 회원쿠폰 ID

    @Setter
    @Column(name = "mc_member_id", nullable = false)
    private Long mcMemberId; // 회원 ID

    @OneToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon; // 쿠폰 (1:1)

    public MemberCoupon(Long mcMemberId, Coupon coupon) {
        this.mcMemberId = mcMemberId;
        this.coupon = coupon;
    }
}
