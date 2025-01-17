package com.nhnacademy.boostorecoupon.repository;

import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

    Page<CouponPolicy> findByCouponActiveOrderByIdAsc(boolean couponActive, Pageable pageable); // 활성화 된 쿠폰정책 목록

    Optional<CouponPolicy> findByName(String name); // 쿠폰정책 이름으로 쿠폰정책 객체 조회

    @Query("SELECT c.couponPolicy FROM Coupon c WHERE c.id = :couponId")
    Optional<CouponPolicy> findCouponPolicyByCouponId(@Param("couponId") Long couponId); // 쿠폰 ID 로 쿠폰정책 객체 조회
}
