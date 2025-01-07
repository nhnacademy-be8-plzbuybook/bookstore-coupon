package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSearchRequestDto;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupon-targets")
public class CouponTargetController {
    private final CouponTargetService couponTargetService;

    /**
     * 쿠폰대상 생성
     * POST /api/coupon-targets
     */
    @PostMapping
    public ResponseEntity<CouponTargetResponseDto> createCouponTarget(@RequestBody @Valid CouponTargetSaveRequestDto couponTargetSaveRequestDto) {
        CouponTargetResponseDto couponTargetResponseDto = couponTargetService.createCouponTarget(couponTargetSaveRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(couponTargetResponseDto);
    }

    /**
     * 특정 쿠폰정책에 속하는 쿠폰대상 목록 조회
     * GET /api/coupon-targets?policy-id={policyId}&page={page}&pageSize={pageSize}
     */
    @GetMapping
    public ResponseEntity<Page<CouponTargetGetResponseDto>> getCouponTargetsByPolicy(@RequestParam("policy-id") @Min(0) Long policyId,
                                                                                     @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                                     @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        CouponTargetSearchRequestDto couponTargetSearchRequestDto = new CouponTargetSearchRequestDto(policyId, page, pageSize);
        Page<CouponTargetGetResponseDto> responseDto = couponTargetService.getCouponTargetsByPolicyId(couponTargetSearchRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
