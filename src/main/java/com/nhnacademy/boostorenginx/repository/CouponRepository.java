package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code); // 쿠폰 코드로 쿠폰 객체 조회

}
