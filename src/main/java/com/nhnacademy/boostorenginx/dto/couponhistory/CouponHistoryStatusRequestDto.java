package com.nhnacademy.boostorenginx.dto.couponhistory;

public record CouponHistoryStatusRequestDto(
        String status,
        int page,
        int pageSize
) {
}
