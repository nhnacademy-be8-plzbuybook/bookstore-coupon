package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.welcome.WelComeCouponRequestDto;
import com.nhnacademy.boostorenginx.service.WelcomeCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class WelcomeCouponController {
    private final WelcomeCouponService welcomeCouponService;

    @PostMapping("/coupons/welcome")
    public ResponseEntity<String> issueWelcomeCoupon(@RequestBody WelComeCouponRequestDto requestDto) {
        welcomeCouponService.issueWelcomeCoupon(requestDto);
        return ResponseEntity.ok("웰컴 쿠폰이 성공적으로 발급되었습니다");
    }
}
