package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponTargetRepository extends JpaRepository<CouponTarget, Long> {
    List<CouponTarget> findByCouponPolicy(CouponPolicy couponPolicy); // 쿠폰정책으로 쿠폰대상들 조회

    List<CouponTarget> findByCouponPolicy_Id(Long couponPolicyId); // 쿠폰정책 ID 로 쿠폰대상들 조회

    Optional<CouponTarget> findByTargetId(Long targetId); // 쿠폰대상 ID 로 쿠폰대상 객체 조회

    Page<CouponTarget> findByCouponPolicy(CouponPolicy couponPolicy, Pageable pageable); // 쿠폰대상 페이지블
}
