package com.nhnacademy.boostorenginx.dto.couponhistory;

import com.nhnacademy.boostorenginx.entity.CouponHistory;

import java.time.LocalDateTime;

public record CouponHistoryFindResponseDto(
        Long historyId,
        String status,
        LocalDateTime changeDate,
        String reason,
        Long couponId
) {
    public static CouponHistoryFindResponseDto fromEntity(CouponHistory couponHistory) {
        return new CouponHistoryFindResponseDto(
                couponHistory.getId(),
                couponHistory.getStatus().toString(),
                couponHistory.getChangeDate(),
                couponHistory.getReason(),
                couponHistory.getCoupon().getId()
        );
    }
}
