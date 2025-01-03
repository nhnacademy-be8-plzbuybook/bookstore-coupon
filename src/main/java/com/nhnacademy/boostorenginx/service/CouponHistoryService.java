package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryDuringRequestDto;
import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryFindRequestDto;
import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryStatusRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import org.springframework.data.domain.Page;

public interface CouponHistoryService {

    Page<CouponHistory> getHistoryByCouponId(CouponHistoryFindRequestDto dto); // 쿠폰 ID 로 해당 쿠폰의 쿠폰이력 목록 조회

    Page<CouponHistory> getHistoryByStatus(CouponHistoryStatusRequestDto dto); // 쿠폰상태로 쿠폰이력 목록 조회

    Page<CouponHistory> getHistoryDate(CouponHistoryDuringRequestDto dto); // 특정 기간사이에 존재하는 쿠폰이력 목록 조회
}
