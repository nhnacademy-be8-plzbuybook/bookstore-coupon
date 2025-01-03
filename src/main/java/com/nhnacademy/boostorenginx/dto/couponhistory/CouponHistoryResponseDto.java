package com.nhnacademy.boostorenginx.dto.couponhistory;

import com.nhnacademy.boostorenginx.entity.CouponHistory;

import java.time.LocalDateTime;

public record CouponHistoryResponseDto(
        Long historyId,
        String status,
        LocalDateTime changeDate,
        String reason,
        Long couponId
) {
    public static CouponHistoryResponseDto fromEntity(CouponHistory couponHistory) {
        return new CouponHistoryResponseDto(
                couponHistory.getId(),
                couponHistory.getStatus().toString(),
                couponHistory.getChangeDate(),
                couponHistory.getReason(),
                couponHistory.getCoupon().getId()
        );
    }
}