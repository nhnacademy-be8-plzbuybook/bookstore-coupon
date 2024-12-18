package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {
    List<CouponHistory> findByCoupon(Coupon coupon); // 쿠폰으로 쿠폰변경이력 리스트 조회

    List<CouponHistory> findByCoupon_id(Long couponId); // 쿠폰 ID 로 쿠폰변경이력 리스트 조회

    List<CouponHistory> findByCouponAndStatus(Coupon coupon, Status status); // 쿠폰과 Status 를 기준으로 CouponHistory 리스트 조회
}
