package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface CouponHistoryRepository extends JpaRepository<CouponHistory, Long> {

    Page<CouponHistory> findByCoupon_idOrderByCouponIdAsc(Long couponId, Pageable pageable); // 쿠폰 ID 로 쿠폰변경이력 목록 조회

    Page<CouponHistory> findByStatusOrderByChangeDateAsc(Status status, Pageable pageable); // Status 에 해당하는 쿠폰변견이력 목록 조회(오름차순)

    @Query("SELECT ch FROM CouponHistory ch WHERE ch.changeDate BETWEEN :start AND :end ORDER BY ch.changeDate ASC")
    Page<CouponHistory> findChangeDate(LocalDateTime start, LocalDateTime end, Pageable pageable); // 특정 기간 사이 쿠폰변경이력 목록 조회
}
