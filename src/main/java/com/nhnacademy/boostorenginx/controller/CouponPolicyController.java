//package com.nhnacademy.boostorenginx.controller;
//
//import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
//import com.nhnacademy.boostorenginx.dto.CouponPolicySaveResponseDto;
//import com.nhnacademy.boostorenginx.service.CouponPolicyService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RequestMapping("/api/coupon-policies")
//@RestController
//public class CouponPolicyController {
//    private final CouponPolicyService couponPolicyService;
//
//    @PostMapping
//    public ResponseEntity<?> createCouponPolicy(@RequestBody CouponPolicySaveRequestDto saveRequest) {
//        long couponPolicyId = couponPolicyService.createCouponPolicy(saveRequest);
//        CouponPolicySaveResponseDto response = new CouponPolicySaveResponseDto(couponPolicyId);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//}
