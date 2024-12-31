package com.nhnacademy.boostorenginx.dto.coupon;

public record CouponFindCouponPolicyIdRequestDto(
        Long policyId,
        int page,
        int pageSize
) {
}
