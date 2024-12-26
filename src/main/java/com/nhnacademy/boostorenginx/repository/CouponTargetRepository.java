package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponTargetRepository extends JpaRepository<CouponTarget, Long> {

    Page<CouponTarget> findByCouponPolicy_IdOrderByIdAsc(Long couponPolicyId, Pageable pageable); // 쿠폰정책 ID 로 특정 쿠폰정책에 속한 쿠폰대상 조회

    Page<CouponTarget> findByCouponPolicyOrderByIdAsc(CouponPolicy couponPolicy, Pageable pageable); // 페이징 처리된 특정 쿠폰 정책 대상 조회

    boolean existsByCtTargetId(Long ctTargetId); // 대상 ID 에 해당하는 쿠폰대상이 존재하는지 확인
}
