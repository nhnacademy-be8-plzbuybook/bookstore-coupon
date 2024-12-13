package com.nhnacademy.boostorenginx.entity;

import com.nhnacademy.boostorenginx.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id; // 쿠폰아이디
    private String code; // 쿠폰코드 -> UUID 사용

    @Setter
    private Status status; // 쿠폰사용여부

    private LocalDateTime issuedAt; // 쿠폰발급일자

    @Setter
    private LocalDateTime expiredAt; // 쿠폰만료일자

    @ManyToOne
    @JoinColumn(name = "coupon_policy_id")
    private CouponPolicy couponPolicy;

    public Coupon(Status status, LocalDateTime issuedAt, LocalDateTime expiredAt, CouponPolicy couponPolicy) {
        this.code = UUID.randomUUID().toString();
        this.status = status;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.couponPolicy = couponPolicy;
    }
}
