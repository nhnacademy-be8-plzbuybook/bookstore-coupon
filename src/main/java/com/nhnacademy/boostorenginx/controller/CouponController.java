package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.CouponCreateDto;
import com.nhnacademy.boostorenginx.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<?> registerCoupon(@RequestBody CouponCreateDto dto) {
        Long couponId = couponService.registerCoupon(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(couponId);
    }
}
