package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponHistoryService {

    Page<CouponHistory> getHistoryByCouponId(Long couponId, Pageable pageable); // 쿠폰 ID 로 해당 쿠폰이력 조회

    Page<CouponHistory> getHistoryByStatus(Status status, Pageable pageable); // 상태로 쿠폰이력 목록 조회

}
