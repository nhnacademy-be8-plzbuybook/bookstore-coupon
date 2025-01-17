package com.nhnacademy.boostorecoupon.service;

import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryDuringRequestDto;
import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryFindRequestDto;
import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryStatusRequestDto;
import com.nhnacademy.boostorecoupon.entity.CouponHistory;
import org.springframework.data.domain.Page;

public interface CouponHistoryService {

    Page<CouponHistory> getHistoryByCouponId(CouponHistoryFindRequestDto dto); // 쿠폰 ID 로 해당 쿠폰의 쿠폰이력 목록 조회

    Page<CouponHistory> getHistoryByStatus(CouponHistoryStatusRequestDto dto); // 쿠폰상태로 쿠폰이력 목록 조회

    Page<CouponHistory> getHistoryDate(CouponHistoryDuringRequestDto dto); // 특정 기간사이에 존재하는 쿠폰이력 목록 조회
}
