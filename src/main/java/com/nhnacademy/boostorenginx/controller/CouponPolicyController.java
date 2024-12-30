package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.couponpolicy.*;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<CouponPolicySaveResponseDto> createCouponPolicy(@RequestBody CouponPolicySaveRequestDto couponPolicySaveRequestDto) {
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);
        CouponPolicySaveResponseDto couponPolicySaveResponseDto = new CouponPolicySaveResponseDto(couponPolicyResponseDto.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(couponPolicySaveResponseDto);
    }

    // 쿠폰정책 ID 로 쿠폰정책 검색
    @GetMapping("/policies/{id}")
    public ResponseEntity<CouponPolicyResponseDto> findById(@PathVariable("id") Long couponPolicyId) {
        CouponPolicyIdRequestDto couponPolicyIdRequestDto = new CouponPolicyIdRequestDto(couponPolicyId);
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findById(couponPolicyIdRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    // 쿠폰정책 이름으로 검색
    @GetMapping("/policies/name/{name}")
    public ResponseEntity<CouponPolicyResponseDto> findByName(@PathVariable String name) {
        CouponPolicyNameRequestDto couponPolicyNameRequestDto = new CouponPolicyNameRequestDto(name);
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findByName(couponPolicyNameRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    // 활성화된 쿠폰정책 조회
    @GetMapping("/policies/active")
    public ResponseEntity<Page<CouponPolicyResponseDto>> findActiveCouponPolicies(Pageable pageable) {
        Page<CouponPolicyResponseDto> activePolicies = couponPolicyService.findActiveCouponPolicy(true, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(activePolicies);
    }

    // 쿠폰정책에 쿠폰대상 추가
    @PostMapping("/policies/addTargets")
    public ResponseEntity<Void> addCouponTargets(@RequestBody CouponTargetAddRequestDto addRequest) {
        couponPolicyService.addTargetToPolicy(addRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
