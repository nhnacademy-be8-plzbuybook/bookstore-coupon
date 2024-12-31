package com.nhnacademy.boostorenginx.dto.calculation;

import java.math.BigDecimal;

public record CouponCalculationRequestDto(
        Long memberId, // 회원 고유 ID
        Long couponId, // 쿠폰 고유 ID
        Long couponPolicy, // 쿠폰정책 고유 ID
        BigDecimal productPrice, // 주문상품 가격(개별)
        Integer quantity // 주문상품 수량
) {
}
