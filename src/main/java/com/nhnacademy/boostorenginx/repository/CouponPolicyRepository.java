package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {
    List<CouponPolicy> findByCouponActive(boolean couponActive); // 쿠폰정책활성화여부 로 쿠폰정책 리스트 조회 -> 왜 필요할까??

    Optional<CouponPolicy> findByName(String name); // 쿠폰정책 이름으로 조회

    List<CouponPolicy> findByMinimumAmountLessThanEqual(BigDecimal amount); // 최소금액조건 조회

    Page<CouponPolicy> findAllBy(Pageable pageable); // 모든 쿠폰정책 페이지블
}
