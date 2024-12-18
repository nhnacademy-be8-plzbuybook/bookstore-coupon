//package com.nhnacademy.boostorenginx.controller;
//
//import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
//import com.nhnacademy.boostorenginx.dto.CouponPolicySaveResponseDto;
//import com.nhnacademy.boostorenginx.service.CouponPolicyService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RequestMapping("/api/coupon-policies")
//@RestController
//public class CouponPolicyController {
//    private final CouponPolicyService couponPolicyService;
//
//    // 쿠폰정책 등록
//    @PostMapping
//    public ResponseEntity<?> createCouponPolicy(@RequestBody CouponPolicySaveRequestDto saveRequest) {
//        long couponPolicyId = couponPolicyService.createCouponPolicy(saveRequest);
//        CouponPolicySaveResponseDto response = new CouponPolicySaveResponseDto(couponPolicyId);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    // 쿠폰대상 추가
//    @PostMapping("/{policyId}/targets")
//    public ResponseEntity<?> addCouponTargets(
//            @PathVariable Long policyId, @RequestBody List<Long> targetIdList) {
//        couponPolicyService.addCouponTargetList(policyId, targetIdList);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//}
