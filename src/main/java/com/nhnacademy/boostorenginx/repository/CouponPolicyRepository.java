package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

    Page<CouponPolicy> findByCouponActiveOrderByIdAsc(boolean couponActive, Pageable pageable); // 쿠폰정책활성화 목록 조회

    Optional<CouponPolicy> findByName(String name); // 쿠폰정책 이름 검색

}
