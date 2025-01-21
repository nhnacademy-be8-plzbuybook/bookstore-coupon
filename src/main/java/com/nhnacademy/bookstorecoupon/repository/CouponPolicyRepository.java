package com.nhnacademy.bookstorecoupon.repository;

import com.nhnacademy.bookstorecoupon.entity.CouponPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

    Page<CouponPolicy> findByCouponActiveOrderByIdAsc(boolean couponActive, Pageable pageable); // 활성화 된 쿠폰정책 목록

    @Query("SELECT c FROM CouponPolicy c WHERE c.couponActive = :couponActive AND c.endDate < :now ORDER BY c.id ASC")
    Page<CouponPolicy> findExpiredCouponPolicies(@Param("couponActive") boolean couponActive, @Param("now") LocalDateTime now, Pageable pageable); // 활성중인 쿠폰정책중 기한이 만료된 쿠폰정책 목록 조회

    Optional<CouponPolicy> findByName(String name); // 쿠폰정책 이름으로 쿠폰정책 객체 조회

    @Query("SELECT c.couponPolicy FROM Coupon c WHERE c.id = :couponId")
    Optional<CouponPolicy> findCouponPolicyByCouponId(@Param("couponId") Long couponId); // 쿠폰 ID 로 쿠폰정책 객체 조회

}
