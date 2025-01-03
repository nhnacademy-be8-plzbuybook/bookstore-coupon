package com.nhnacademy.boostorenginx.dto.coupontarget;

import jakarta.validation.constraints.Min;

public record CouponTargetSeearchReqeustDto(
        @Min(0) Long policyId,
        @Min(0) int page,
        @Min(1) int pageSize
) {
}
