package com.nhnacademy.boostorenginx.entity;

import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.NotFoundMemberCouponException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class MemberCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberCouponId; // 회원 쿠폰 ID -> auto_increment
    private Long mcMemberId; // 회원 ID -> MSA 구조 에서 다른 DB(회원) 의 식별키를 외래키로 받아야함

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon; // 쿠폰 (외래키)

    public MemberCoupon(Long mcMemberId, Coupon coupon) {
        this.mcMemberId = mcMemberId;
        this.coupon = coupon;
    }

}
