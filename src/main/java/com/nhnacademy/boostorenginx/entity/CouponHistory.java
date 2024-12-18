package com.nhnacademy.boostorenginx.entity;

import com.nhnacademy.boostorenginx.enums.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class CouponHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_history_id")
    private Long id; // 쿠폰변경이력 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // 쿠폰상태

    @Column(nullable = false)
    private LocalDateTime changeDate; // 변경일자

    @Column(nullable = false)
    private String reason; // 변경사유

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon; // 외래키

    @Builder
    public CouponHistory(Status status, LocalDateTime changeDate, String reason, Coupon coupon) {
        this.status = status;
        this.changeDate = changeDate;
        this.reason = reason;
        this.coupon = coupon;
    }
}
