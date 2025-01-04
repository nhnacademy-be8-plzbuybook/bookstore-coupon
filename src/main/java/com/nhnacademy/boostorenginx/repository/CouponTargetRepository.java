package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponTargetRepository extends JpaRepository<CouponTarget, Long> {

    Page<CouponTarget> findByCouponPolicy_IdOrderByIdAsc(Long couponPolicyId, Pageable pageable); // 쿠폰정책 ID 로 특정 쿠폰정책에 속한 쿠폰대상 목록 조회

    boolean existsByCtTargetId(Long ctTargetId); // (도메인)대상 ID 에 해당하는 쿠폰대상 존재여부 확인

}
