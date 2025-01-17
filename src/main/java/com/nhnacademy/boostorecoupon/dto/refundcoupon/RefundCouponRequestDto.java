package com.nhnacademy.boostorecoupon.dto.refundcoupon;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RefundCouponRequestDto {
    private Long couponId; // 쿠폰 식별키
    private Long memberId; // 회원 식별키
}
