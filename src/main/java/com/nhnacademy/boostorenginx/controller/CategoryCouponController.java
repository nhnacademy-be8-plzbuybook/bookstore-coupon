package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.service.CategoryCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class CategoryCouponController {
    private final CategoryCouponService categoryCouponService;

    // 카테고리 쿠폰 생성
    @PostMapping("/category/{keyword}")
    public ResponseEntity<String> categoryCoupon(@PathVariable("keyword") String keyword) {
        //dto 정리 하고 만들어야됨 -> 회원정책에 필요한 정보들
//        categoryCouponService.issueCategoryCoupon();
        return ResponseEntity.ok("카테고리 쿠폰이 성공적으로 발급되었습니다");
    }

}
