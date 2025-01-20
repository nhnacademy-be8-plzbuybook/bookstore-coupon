package com.nhnacademy.boostorecoupon.controller;

import com.nhnacademy.boostorecoupon.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.boostorecoupon.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.boostorecoupon.dto.calculation.ValidationCouponCalculation;
import com.nhnacademy.boostorecoupon.service.CouponCalculationService;
import com.nhnacademy.boostorecoupon.shoppingmall.service.MemberService;
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
    private final MemberService memberService;

    /**
     * 주문금액 할인계산
     * POST /api/member-coupons/member/{coupon-id}/calculate
     *
     * @param couponId
     * @param calculationRequestDto
     * @return
     */
    @PostMapping("/member/{coupon-id}/calculate")
    public ResponseEntity<CouponCalculationResponseDto> applyOrderProductCoupon(@PathVariable("coupon-id") Long couponId,
                                                                                @RequestBody @Valid CouponCalculationRequestDto calculationRequestDto) {
        CouponCalculationResponseDto couponCalculationResponseDto = couponCalculationService.applyOrderProductCoupon(couponId, calculationRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(couponCalculationResponseDto);
    }

    @PostMapping("/member/{coupon-id}/validation")
    public ResponseEntity<ValidationCouponCalculation> validateCouponCalculation(@PathVariable("coupon-id") Long couponId, @RequestBody @Valid CouponCalculationRequestDto calculationRequestDto) {
        ValidationCouponCalculation validationCouponCalculation = couponCalculationService.validateCouponCalculation(couponId, calculationRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(validationCouponCalculation);
    }
}
