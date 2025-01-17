package com.nhnacademy.boostorecoupon.controller;

import com.nhnacademy.boostorecoupon.dto.refundcoupon.RefundCouponRequestDto;
import com.nhnacademy.boostorecoupon.service.RefundCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class RefundCouponController {
    private final RefundCouponService refundCouponService;

    /**
     * POST /api/coupons/refund
     * @param refundCouponRequestDto : Long couponId (쿠폰식별키), Long mcMemberId (회원 식별키)
     * @return "환불이 완료되었습니다"
     */
    @PostMapping("/refund")
    public ResponseEntity<String> refundCoupon(@RequestBody RefundCouponRequestDto refundCouponRequestDto) {
        refundCouponService.refundCoupon(refundCouponRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body("환불이 완료되었습니다");
    }
}
