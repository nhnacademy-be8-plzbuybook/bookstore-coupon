package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.welcome.WelComeCouponRequestDto;
import com.nhnacademy.boostorenginx.service.impl.WelcomeCouponServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class WelcomeCouponController {
    private final WelcomeCouponServiceImpl welcomeCouponServiceImpl;

    @PostMapping("/welcome")
    public ResponseEntity<String> issueWelcomeCoupon(@RequestBody WelComeCouponRequestDto requestDto) {
        welcomeCouponServiceImpl.issueWelcomeCoupon(requestDto);
        return ResponseEntity.ok("웰컴 쿠폰이 성공적으로 발급되었습니다");
    }
}
