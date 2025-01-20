package com.nhnacademy.boostorecoupon.controller;

import com.nhnacademy.boostorecoupon.dto.couponpolicy.*;
import com.nhnacademy.boostorecoupon.service.CouponPolicyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/coupon-policies")
@RestController
public class CouponPolicyController {
    private final CouponPolicyService couponPolicyService;

    /**
     * 쿠폰정책 등록
     * POST /api/coupon-policies
     */
    @PostMapping
    public ResponseEntity<CouponPolicyResponseDto> createCouponPolicy(@RequestBody @Valid CouponPolicySaveRequestDto couponPolicySaveRequestDto) {
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(couponPolicyResponseDto);
    }

    /**
     * 쿠폰정책 전체 조회
     * GET /api/coupon-policies
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<CouponPolicyResponseDto>> findAllCouponPolicies(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CouponPolicyResponseDto> couponPolicyResponseDto = couponPolicyService.findAllCouponPolicies(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    /**
     * 활성화된 쿠폰정책 목록 조회
     * GET /api/coupon-policies/active
     */
    @GetMapping("active")
    public ResponseEntity<Page<CouponPolicyResponseDto>> findActiveCouponPolicies(@RequestParam(defaultValue = "true") boolean couponActive,
                                                                                  @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                                  @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        CouponPolicyActiveRequestDto couponPolicyActiveRequestDto = new CouponPolicyActiveRequestDto(couponActive, page, pageSize);
        Page<CouponPolicyResponseDto> activePolicies = couponPolicyService.findActiveCouponPolicy(couponPolicyActiveRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(activePolicies);
    }

    /**
     * 쿠폰정책 ID로 검색
     * GET /api/coupon-policies/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CouponPolicyResponseDto> findById(@PathVariable("id") @Min(0) Long couponPolicyId) {
        CouponPolicyIdRequestDto couponPolicyIdRequestDto = new CouponPolicyIdRequestDto(couponPolicyId);
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findById(couponPolicyIdRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    /**
     * 쿠폰정책 이름으로 검색
     * GET /api/coupon-policies/search
     */
    @GetMapping("/search")
    public ResponseEntity<CouponPolicyResponseDto> findByName(@RequestParam("name") @NotBlank String name) {
        CouponPolicyNameRequestDto couponPolicyNameRequestDto = new CouponPolicyNameRequestDto(name);
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findByName(couponPolicyNameRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    /**
     * 쿠폰 ID 로 쿠폰정책 조회
     * GET /api/coupon-policies/coupon/{coupon-id}
     */
    @GetMapping("/coupon/{coupon-id}")
    public ResponseEntity<CouponPolicyResponseDto> findCouponPolicyByCouponId(@PathVariable("coupon-id") @Min(0) Long couponId) {
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findCouponPolicyById(couponId);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    /**
     * 쿠폰정책에 쿠폰대상 추가
     * POST /api/coupon-policies/{policy-id}/targets
     */
    @PostMapping("/{policy-id}/targets")
    public ResponseEntity<String> addCouponTargets(@PathVariable("policy-id") @Min(0) Long policyId, @RequestBody @Valid Long ctTargetId) {
        CouponTargetAddRequestDto ctTargetAddRequestDto = new CouponTargetAddRequestDto(policyId, ctTargetId);
        couponPolicyService.addTargetToPolicy(ctTargetAddRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("쿠폰정책에 쿠폰대상이 성공적으로 추가되었습니다");
    }

}
