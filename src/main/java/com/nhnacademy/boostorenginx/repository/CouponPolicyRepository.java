package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

    Page<CouponPolicy> findByCouponActiveOrderByCouponActiveAsc(boolean couponActive, Pageable pageable); // 쿠폰정책활성화 목록 조회
}
