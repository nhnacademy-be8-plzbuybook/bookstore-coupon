package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.couponpolicy.*;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CouponPolicyController {
    private final CouponPolicyService couponPolicyService;

    // 쿠폰정책 등록
    @PostMapping("/coupon-policies")
    public ResponseEntity<?> createCouponPolicy(@RequestBody CouponPolicySaveRequestDto saveRequest) {
        long couponPolicyId = couponPolicyService.createCouponPolicy(saveRequest); // DB 에 쿠폰정책을 만들어서 저장하고 반환값으로 ID 를 받아옴
        CouponPolicySaveResponseDto response = new CouponPolicySaveResponseDto(couponPolicyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 이름으로 쿠폰정책 검색
    @GetMapping("/coupon-policies/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        CouponPolicyNameRequestDto requestDto = new CouponPolicyNameRequestDto(name);

        CouponPolicy policy = couponPolicyService.findByName(requestDto).orElseThrow(
                () -> new NotFoundCouponPolicyException("CouponPolicy not found")
        );
        CouponPolicyNameResponseDto response = CouponPolicyNameResponseDto.fromEntity(policy);

        return ResponseEntity.ok(response);
    }

    // ID 로 쿠폰정책 검색
    @GetMapping("/coupon-policies/{coupon-id}")
    public ResponseEntity<?> findById(@PathVariable("coupon-id") Long couponId) {
        CouponPolicyIdRequestDto idRequest = new CouponPolicyIdRequestDto(couponId);
        CouponPolicy couponPolicy = couponPolicyService.findById(idRequest).orElseThrow(
                () -> new NotFoundCouponPolicyException("ID 에 해당하는 쿠폰정책을 찾지 못헀습니다: " + idRequest.couponPolicyId())
        );
        CouponPolicyIdResponseDto response = new CouponPolicyIdResponseDto(
                couponPolicy.getId(),
                couponPolicy.getName(),
                couponPolicy.getSaleType(),
                couponPolicy.getMinimumAmount(),
                couponPolicy.getDiscountLimit(),
                couponPolicy.getDiscountRatio(),
                couponPolicy.isStackable(),
                couponPolicy.getCouponScope(),
                couponPolicy.getStartDate(),
                couponPolicy.getEndDate(),
                couponPolicy.isCouponActive()
        );
        return ResponseEntity.ok(response); // 성공시 200 반환
    }

    // 쿠폰정책에 쿠폰대상 추가
    @PostMapping("/coupon-policies/addTargets")
    public ResponseEntity<?> addCouponTargets(@RequestBody CouponTargetAddRequestDto addRequest) {
        couponPolicyService.addTargetToPolicy(addRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
