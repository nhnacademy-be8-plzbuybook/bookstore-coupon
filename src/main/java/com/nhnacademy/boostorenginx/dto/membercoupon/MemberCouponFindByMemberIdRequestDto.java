package com.nhnacademy.boostorenginx.dto.membercoupon;

public record MemberCouponFindByMemberIdRequestDto(
        Long memberId,
        int page,
        int pageSize
) {
}
