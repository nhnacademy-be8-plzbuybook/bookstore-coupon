//package com.nhnacademy.boostorenginx.controller;
//
//
//import com.nhnacademy.boostorenginx.service.CouponTargetService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/coupon-targets")
//public class CouponTargetController {
//    private final CouponTargetService couponTargetService;
//
//    @PostMapping
//    public ResponseEntity<String> createCouponTarget(@RequestParam Long policyId, @RequestParam Long targetId) {
//        couponTargetService.createCouponTarget(policyId, targetId);
//        return ResponseEntity.ok("Coupon Target 이 생성되었습니다");
//    }
//}
