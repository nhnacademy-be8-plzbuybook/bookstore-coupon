package com.nhnacademy.boostorenginx.dto.coupon;

public record CouponFindStatusRequestDto(
        String status,
        int page,
        int pageSize
) {
}
