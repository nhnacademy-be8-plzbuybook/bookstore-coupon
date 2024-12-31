package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.boostorenginx.service.CouponCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class CouponCalculationController {

    private final CouponCalculationService couponCalculationService;

    // 주문상품에 할인 쿠폰을 적용하는 api
    @GetMapping("/order-products/{orderProductId}")
    public ResponseEntity<CouponCalculationResponseDto> applyOrderProductCoupon(@PathVariable("orderProductId") Long orderProductId, @RequestBody CouponCalculationRequestDto calculationRequestDto) {
        CouponCalculationResponseDto couponCalculationResponseDto = couponCalculationService.applyOrderProductCoupon(calculationRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(couponCalculationResponseDto);
    }
}
