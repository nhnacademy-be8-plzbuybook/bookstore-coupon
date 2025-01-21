package com.nhnacademy.bookstorecoupon.controller;

import com.nhnacademy.bookstorecoupon.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.bookstorecoupon.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.bookstorecoupon.dto.calculation.ValidationCouponCalculation;
import com.nhnacademy.bookstorecoupon.dto.calculation.ValidationCouponCalculationRequestDto;
import com.nhnacademy.bookstorecoupon.service.CouponCalculationService;
import com.nhnacademy.bookstorecoupon.shoppingmall.service.MemberService;
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
     * @param couponId : 쿠폰 ID
     * @param calculationRequestDto : BigDecimal price, Long memberId
     * @return CouponCalculationResponseDto : BigDecimal discountAmount, BigDecimal originalPrice, BigDecimal calculationPrice
     */
    @PostMapping("/member/{coupon-id}/calculate")
    public ResponseEntity<CouponCalculationResponseDto> applyOrderProductCoupon(@PathVariable("coupon-id") Long couponId,
                                                                                @RequestBody @Valid CouponCalculationRequestDto calculationRequestDto) {
        CouponCalculationResponseDto couponCalculationResponseDto = couponCalculationService.applyOrderProductCoupon(couponId, calculationRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(couponCalculationResponseDto);
    }

    /**
     * 할인쿠폰 계산검증
     * @param couponId : 쿠폰 ID
     * @param validationCouponCalculationRequestDto : BigDecimal price
     * @return CouponCalculationResponseDto : BigDecimal discountAmount, BigDecimal originalPrice, BigDecimal calculationPrice
     */
    @PostMapping("/member/{coupon-id}/validation")
    public ResponseEntity<ValidationCouponCalculation> validateCouponCalculation(@PathVariable("coupon-id") Long couponId, @RequestBody @Valid ValidationCouponCalculationRequestDto validationCouponCalculationRequestDto) {
        ValidationCouponCalculation validationCouponCalculation = couponCalculationService.validateCouponCalculation(couponId, validationCouponCalculationRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(validationCouponCalculation);
    }
}
