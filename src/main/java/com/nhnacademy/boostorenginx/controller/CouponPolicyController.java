package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.couponpolicy.*;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class CouponPolicyController {
    private final CouponPolicyService couponPolicyService;

    // 쿠폰정책 등록
    @PostMapping("/policies")
    public ResponseEntity<CouponPolicyResponseDto> createCouponPolicy(@RequestBody @Valid CouponPolicySaveRequestDto couponPolicySaveRequestDto) {
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(couponPolicyResponseDto);
    }

    // 쿠폰정책 ID 로 쿠폰정책 검색
    @GetMapping("/policies")
    public ResponseEntity<CouponPolicyResponseDto> findById(@Valid CouponPolicyIdRequestDto couponPolicyIdRequestDto) {
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findById(couponPolicyIdRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    // 쿠폰정책 이름으로 쿠폰정책 검색
    @GetMapping("/policies/name")
    public ResponseEntity<CouponPolicyResponseDto> findByName(@Valid CouponPolicyNameRequestDto couponPolicyNameRequestDto) {
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findByName(couponPolicyNameRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    // 활성화된 쿠폰정책 목록 조회
    @GetMapping("/policies/active")
    public ResponseEntity<Page<CouponPolicyResponseDto>> findActiveCouponPolicies(@Valid CouponPolicyActiveRequestDto couponPolicyActiveRequestDto) {
        Page<CouponPolicyResponseDto> activePolicies = couponPolicyService.findActiveCouponPolicy(couponPolicyActiveRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(activePolicies);
    }

    // 쿠폰정책에 쿠폰대상 추가
    @PostMapping("/policies/addTargets")
    public ResponseEntity<String> addCouponTargets(@RequestBody @Valid CouponTargetAddRequestDto addRequest) {
        couponPolicyService.addTargetToPolicy(addRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("쿠폰정책에 쿠폰대상이 성공적으로 추가되었습니다");
    }
}
