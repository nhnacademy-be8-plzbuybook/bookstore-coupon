package com.nhnacademy.boostorenginx.entity;

import com.nhnacademy.boostorenginx.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon; // 식별관계 - 부모테이블(coupon)의 PK를 가져옴

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime changeDate;

    @Column(nullable = false)
    private String reason;

    @Builder
    public CouponHistory(Coupon coupon, Status status, LocalDateTime changeDate, String reason) {
        this.coupon = coupon;
        this.status = status;
        this.changeDate = changeDate;
        this.reason = reason;
    }

}
