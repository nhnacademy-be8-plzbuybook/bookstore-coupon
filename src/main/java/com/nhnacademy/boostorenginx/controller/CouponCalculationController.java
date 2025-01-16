package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.boostorenginx.dto.calculation.ValidationCouponCalculation;
import com.nhnacademy.boostorenginx.feign.ShoppingMallClient;
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

    private final ShoppingMallClient shoppingMallClient;

    /**
     * 주문금액 할인계산
     * POST /api/member-coupons/member/{coupon-id}/calculate
     * @param email
     * @param couponId
     * @param calculationRequestDto : BigDecimal price
     * @return
     */
    @PostMapping("/member/{coupon-id}/calculate")
    public ResponseEntity<CouponCalculationResponseDto> applyOrderProductCoupon(@RequestHeader("X-USER-ID") String email,
                                                                                @PathVariable("coupon-id") Long couponId,
                                                                                @RequestBody @Valid CouponCalculationRequestDto calculationRequestDto) {
        Long mcMemberId = shoppingMallClient.getMemberIdByEmail(email).getBody(); // 회원 고유 ID

        CouponCalculationResponseDto couponCalculationResponseDto = couponCalculationService.applyOrderProductCoupon(mcMemberId, couponId, calculationRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(couponCalculationResponseDto);
    }

    @PostMapping("/member/{coupon-id}/validation")
    public ResponseEntity<ValidationCouponCalculation> validateCouponCalculation(@PathVariable("coupon-id") Long couponId, @RequestBody @Valid CouponCalculationRequestDto calculationRequestDto) {
        ValidationCouponCalculation validationCouponCalculation = couponCalculationService.validateCouponCalculation(couponId, calculationRequestDto);

        return  ResponseEntity.status(HttpStatus.OK).body(validationCouponCalculation);
    }
}
