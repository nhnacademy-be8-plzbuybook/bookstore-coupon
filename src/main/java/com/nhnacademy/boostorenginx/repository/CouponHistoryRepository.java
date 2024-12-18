package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {

    Page<CouponHistory> findByCouponOrderByCouponAsc(Coupon coupon, Pageable pageable); // 쿠폰으로 쿠폰변경이력 리스트 조회

    Page<CouponHistory> findByCoupon_idOrderByCouponIdAsc(Long couponId, Pageable pageable); // 쿠폰 ID 로 쿠폰변경이력 리스트 조회

    Page<CouponHistory> findByCouponAndStatusOrderByStatusAsc(Coupon coupon, Status status, Pageable pageable); // 쿠폰과 Status 를 기준으로 CouponHistory 리스트 조회
}
