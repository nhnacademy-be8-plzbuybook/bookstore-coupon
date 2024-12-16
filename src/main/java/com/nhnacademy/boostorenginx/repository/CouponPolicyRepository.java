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
    List<CouponPolicy> findByCouponActiveTrue (boolean couponActive); // 활성화된 쿠폰 정책 조회

    Optional<CouponPolicy> findByName(String name); // 쿠폰정책 이름으로 조회

    List<CouponPolicy> findBySaleType(SaleType saleType); // 할인타입(금액할인, 비율할인) 으로 조회

    List<CouponPolicy> findByMinimumAmountLessThanEqual(BigDecimal amount); // 최소금액조건 조회

    Page<CouponPolicy> findAllBy(Pageable pageable); // 모든 쿠폰정책 페이지블
}
