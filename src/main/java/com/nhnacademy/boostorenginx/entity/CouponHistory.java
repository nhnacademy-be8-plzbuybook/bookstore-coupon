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

    @EmbeddedId
    private CouponHistoryPrimaryKey id; // 복합키

    @MapsId("couponId")
    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime changeDate;

    @Column(nullable = false)
    private String reason;

    @Builder
    public CouponHistory(Coupon coupon, Long historyId, Status status, LocalDateTime changeDate, String reason) {
        this.id = new CouponHistoryPrimaryKey(coupon.getId(), historyId);
        this.coupon = coupon;
        this.status = status;
        this.changeDate = changeDate;
        this.reason = reason;
    }
}
