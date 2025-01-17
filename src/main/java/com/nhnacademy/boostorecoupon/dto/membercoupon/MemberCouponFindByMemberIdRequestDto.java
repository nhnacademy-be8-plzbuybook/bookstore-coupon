package com.nhnacademy.boostorecoupon.dto.membercoupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MemberCouponFindByMemberIdRequestDto(
        @Min(0)
        @NotNull
        Long memberId, // 회원 ID
        int page,
        int pageSize
) {
}
