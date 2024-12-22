package com.nhnacademy.boostorenginx.controller;


import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CouponTargetController {
    private final CouponTargetService couponTargetService;

    // 쿠폰대상 생성
    @PostMapping("/coupon-targets")
    public ResponseEntity<Void> createCouponTarget(@RequestBody CouponTargetAddRequestDto requestDto) {
        couponTargetService.createCouponTarget(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
