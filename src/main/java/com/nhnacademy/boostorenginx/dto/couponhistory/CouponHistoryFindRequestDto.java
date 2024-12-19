package com.nhnacademy.boostorenginx.dto.couponhistory;

public record CouponHistoryFindRequestDto(
        Long couponHistoryId,
        int page,
        int pageSize
) {
}
