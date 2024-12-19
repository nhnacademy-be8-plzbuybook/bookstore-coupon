package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponHistoryService {

    Page<CouponHistory> getHistoryByCouponId(Long couponId, Pageable pageable); // 쿠폰 ID 로 해당 쿠폰의 이력 조회

    Page<CouponHistory> getHistoryByCouponAndStatus(Coupon coupon, Status status , Pageable pageable); // 특정 쿠폰 및 상태 로 이력 조회


}
