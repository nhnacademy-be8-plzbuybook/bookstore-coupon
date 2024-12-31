package com.nhnacademy.boostorenginx.dto.couponpolicy;

import jakarta.validation.constraints.Min;

public record CouponPolicyIdRequestDto(
        @Min(0)
        Long couponPolicyId
) {
}
