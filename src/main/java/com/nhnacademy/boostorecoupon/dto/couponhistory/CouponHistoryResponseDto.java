package com.nhnacademy.boostorecoupon.dto.couponhistory;

import com.nhnacademy.boostorecoupon.entity.CouponHistory;

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
