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
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(String code); // 코드로 쿠폰 객체 찾기

    Page<Coupon> findByExpiredAtBeforeOrderByExpiredAtAsc(LocalDateTime currentDateTime, Pageable pageable); // 현재 시간 기준으로 만료된 쿠폰 목록 조회

    @Query("SELECT coupon FROM Coupon coupon WHERE :currentDateTime BETWEEN coupon.issuedAt AND coupon.expiredAt ORDER BY coupon.issuedAt ASC ")
    Page<Coupon> findActiveCoupons(@Param("currentDateTime") LocalDateTime currentDateTime, Pageable pageable); // 현재 시간 기준으로 발급일자 ~ 만료일자 사이인 모든 쿠폰들 조회

    Page<Coupon> findByCouponPolicyOrderByIdAsc(CouponPolicy couponPolicy, Pageable pageable); // 쿠폰정책 객체로 쿠폰 목록 조회

    Page<Coupon> findByStatusOrderByStatusAsc(Status status, Pageable pageable); // 쿠폰 상태로 쿠폰 목록 조회

    Page<Coupon> findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(LocalDateTime expiredAt, Status status, Pageable pageable); // 기한이 만료되고 쿠폰의 상태가 UNUSED 인 쿠폰 목록 조회

}

