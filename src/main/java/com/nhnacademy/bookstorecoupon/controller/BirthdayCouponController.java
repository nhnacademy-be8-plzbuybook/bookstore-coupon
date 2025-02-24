package com.nhnacademy.bookstorecoupon.controller;

import com.nhnacademy.bookstorecoupon.dto.birthday.BirthdayCouponRequestDto;
import com.nhnacademy.bookstorecoupon.service.BirthdayCouponService;
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

    private final BirthdayCouponService birthdayCouponService;

    // 생일쿠폰 발급 API
    @PostMapping("/birthday")
    public ResponseEntity<String> issueBirthdayCoupon(@RequestBody BirthdayCouponRequestDto requestDto) {
        birthdayCouponService.issueBirthdayCoupon(requestDto);
        return ResponseEntity.ok("생일 쿠폰이 성공적으로 발급되었습니다");
    }
}
