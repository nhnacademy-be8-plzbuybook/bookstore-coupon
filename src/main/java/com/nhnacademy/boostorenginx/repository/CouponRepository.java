package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code); // 쿠폰코드로 쿠폰 객체 조회

    List<Coupon> findByExpiredAtBefore(LocalDateTime currentDateTime); // 만료된 쿠폰들 조회(현재시각기준)

    @Query("SELECT coupon FROM Coupon coupon WHERE :cureentDateTime BETWEEN coupon.issuedAt AND coupon.expiredAt")
    List<Coupon> findActiveCoupons(@Param("currentDateTime") LocalDateTime currentDateTime); // 발급일자 ~ 만료일자 사이인 모든 쿠폰들 조회

    List<Coupon> findByCouponPolicy(CouponPolicy couponPolicy); // 쿠폰정책으로 쿠폰들 조회

    Page<Coupon> findByStatus(Status status, Pageable pageable); // 특정상태인 쿠폰들 페이징 조회
}
