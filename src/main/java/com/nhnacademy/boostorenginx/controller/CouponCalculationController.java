package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.boostorenginx.service.CouponCalculationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/member-coupons")
@RestController
public class CouponCalculationController {

    private final CouponCalculationService couponCalculationService;

    /**
     * 특정 쿠폰을 주문 상품에 적용하여 할인 계산
     * [POST] /api/member-coupons/member/{member-id}/coupon/{coupon-id}/calculate
     *
     * @param memberId 회원 고유 ID
     * @param couponId 쿠폰 고유 ID (기본키)
     * @param calculationRequestDto 주문상품 가격 및 수량 데이터
     * @return 할인 적용 결과 (할인 금액, 적용 전 금액, 최종 금액)
     */
    @PostMapping("/member/{member-id}/coupon/{coupon-id}/calculate")
    public ResponseEntity<CouponCalculationResponseDto> applyOrderProductCoupon(@PathVariable("member-id") Long memberId, @PathVariable("coupon-id") Long couponId, @RequestBody @Valid CouponCalculationRequestDto calculationRequestDto) {

        CouponCalculationResponseDto couponCalculationResponseDto = couponCalculationService.applyOrderProductCoupon(memberId, couponId, calculationRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(couponCalculationResponseDto);
    }
}
