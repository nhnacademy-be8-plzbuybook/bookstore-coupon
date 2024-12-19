package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {

    Page<CouponHistory> findByCoupon_idOrderByCouponIdAsc(Long couponId, Pageable pageable); // 쿠폰 ID 로 쿠폰변경이력 리스트 조회

    Page<CouponHistory> findByStatusOrderByChangeDateAsc(Status status, Pageable pageable); // 쿠폰과 Status 를 기준으로 CouponHistory 리스트 조회

    @Query("SELECT ch FROM CouponHistory ch WHERE ch.changeDate BETWEEN :start AND :end ORDER BY ch.changeDate ASC")
    Page<CouponHistory> findChangeDate(LocalDateTime start, LocalDateTime end, Pageable pageable); // 특정 기간의 CouponHistory 조회
}
