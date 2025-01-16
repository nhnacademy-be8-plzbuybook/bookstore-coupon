package com.nhnacademy.boostorecoupon.controller;

import com.nhnacademy.boostorecoupon.dto.birthday.BirthdayCouponRequestDto;
import com.nhnacademy.boostorecoupon.service.impl.BirthdayCouponServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class BirthdayCouponController {

    private final BirthdayCouponServiceImpl birthdayCouponServiceImpl;

    // 생일쿠폰 발급 API
    @PostMapping("/birthday")
    public ResponseEntity<String> issueBirthdayCoupon(@RequestBody BirthdayCouponRequestDto requestDto) {
        birthdayCouponServiceImpl.issueBirthdayCoupon(requestDto);
        return ResponseEntity.ok("생일 쿠폰이 성공적으로 발급되었습니다");
    }
}
