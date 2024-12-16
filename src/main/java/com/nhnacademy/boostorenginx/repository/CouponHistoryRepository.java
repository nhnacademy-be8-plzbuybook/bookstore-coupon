package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {
    List<CouponHistory> findByCoupon(Coupon coupon); // 쿠폰 객체로 조회

    List<CouponHistory> findByCoupon_id(Long couponId); // 쿠폰 ID 로 조회
}
