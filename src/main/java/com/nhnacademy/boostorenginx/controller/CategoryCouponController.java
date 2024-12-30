package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.service.CategoryCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class CategoryCouponController {
    private final CategoryCouponService categoryCouponService;

    // 카테고리 쿠폰 생성
    @PostMapping("/category/{keyword}")
    public ResponseEntity<String> categoryCoupon(@PathVariable("keyword") String keyword, @RequestBody CouponPolicySaveRequestDto couponPolicySaveRequestDto) {
        categoryCouponService.issueCategoryCoupon(keyword, couponPolicySaveRequestDto);
        return ResponseEntity.ok("카테고리 쿠폰이 성공적으로 발급되었습니다");
    }

}