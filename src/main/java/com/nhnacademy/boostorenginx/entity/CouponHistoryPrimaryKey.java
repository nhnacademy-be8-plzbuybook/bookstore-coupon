package com.nhnacademy.boostorenginx.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;


// Coupon -> CouponHistory 식별키
@Embeddable
public class CouponHistoryPrimaryKey implements Serializable {

    @Column(name = "member_id")
    private Long couponId;

    @Column(name = "history_id")
    private Long historyId;

    public CouponHistoryPrimaryKey() { }

    public CouponHistoryPrimaryKey(Long couponId, Long historyId) {
        this.couponId = couponId;
        this.historyId = historyId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
