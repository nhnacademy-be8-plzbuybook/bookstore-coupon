package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.enums.Status;

import java.util.List;

public interface CouponHistoryService {
    CouponHistory createHistory(Coupon coupon, Status status, String reason); // 쿠폰변경이력 생성 메서드

    List<CouponHistory> getHistoryByCoupon(Coupon coupon); // 특정 쿠폰으로 이력 조회

    List<CouponHistory> getHistoryByCouponId(Long couponId); // 쿠폰 ID 로 이력 조회

    List<CouponHistory> getHistoryByCouponAndStatus(Coupon coupon, Status status); // 특정 쿠폰 및 상태 로 이력 조회
}
