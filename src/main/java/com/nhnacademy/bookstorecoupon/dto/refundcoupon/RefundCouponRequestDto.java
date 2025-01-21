package com.nhnacademy.bookstorecoupon.dto.refundcoupon;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefundCouponRequestDto {
    private Long couponId; // 쿠폰 식별키
    private Long memberId; // 회원 식별키
}
