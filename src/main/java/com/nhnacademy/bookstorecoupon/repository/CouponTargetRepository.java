package com.nhnacademy.bookstorecoupon.repository;

import com.nhnacademy.bookstorecoupon.entity.CouponTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponTargetRepository extends JpaRepository<CouponTarget, Long> {

    Page<CouponTarget> findByCouponPolicy_IdOrderByIdAsc(Long couponPolicyId, Pageable pageable); // 쿠폰정책 ID 로 특정 쿠폰정책에 속한 쿠폰대상 목록 조회

}
