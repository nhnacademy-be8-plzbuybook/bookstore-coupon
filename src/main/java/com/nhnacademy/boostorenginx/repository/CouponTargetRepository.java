package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponTargetRepository extends JpaRepository<CouponTarget, Long> {
    Optional<CouponTarget> findByTargetId(Long targetId); // ID 로 쿠폰대상 조회

    List<CouponTarget> findByCouponPolicy_Id(Long couponPolicyId); // 쿠폰정책 ID 로 특정 쿠폰정책에 속한 쿠폰대상 조회

    Page<CouponTarget> findByCouponPolicy(CouponPolicy couponPolicy, Pageable pageable); // 페이징 처리된 특정 쿠폰 정책 대상 조회
}
