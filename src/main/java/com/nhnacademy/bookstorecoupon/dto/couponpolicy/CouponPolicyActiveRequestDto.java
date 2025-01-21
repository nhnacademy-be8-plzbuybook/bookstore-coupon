package com.nhnacademy.bookstorecoupon.dto.couponpolicy;

import jakarta.validation.constraints.Min;

public record CouponPolicyActiveRequestDto(
        boolean couponActive, // 쿠폰정책활성화 여부
        @Min(0)
        int page, // pageable 번호
        @Min(1)
        int pageSize // pageable 크기
) {
}
