package com.nhnacademy.boostorecoupon.dto.coupontarget;

import jakarta.validation.constraints.Min;

public record CouponTargetSearchRequestDto(
        @Min(0) Long policyId,
        @Min(0) int page,
        @Min(1) int pageSize
) {
}
