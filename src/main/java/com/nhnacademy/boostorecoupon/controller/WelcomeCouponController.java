package com.nhnacademy.boostorecoupon.controller;

import com.nhnacademy.boostorecoupon.dto.welcome.WelcomeCouponRequestDto;
import com.nhnacademy.boostorecoupon.service.WelcomeCouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/coupons/welcome")
@RestController
public class WelcomeCouponController {
    private final WelcomeCouponService welcomeCouponService;

    @PostMapping
    public ResponseEntity<String> issueWelcomeCoupon(@RequestBody @Valid WelcomeCouponRequestDto requestDto) {
        welcomeCouponService.issueWelcomeCoupon(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("웰컴 쿠폰이 성공적으로 발급되었습니다");
    }
}
