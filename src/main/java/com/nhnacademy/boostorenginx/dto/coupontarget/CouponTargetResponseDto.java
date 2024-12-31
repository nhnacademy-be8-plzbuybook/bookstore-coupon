package com.nhnacademy.boostorenginx.dto.coupontarget;

import com.nhnacademy.boostorenginx.entity.CouponTarget;

public record CouponTargetResponseDto(
        Long couponTargetId,
        Long couponPolicyId,
        Long ctTargetId
) {
    public static CouponTargetResponseDto fromEntity(CouponTarget couponTarget) {
        return new CouponTargetResponseDto(
                couponTarget.getId(),
                couponTarget.getCouponPolicy().getId(),
                couponTarget.getCtTargetId()
        );
    }
}
