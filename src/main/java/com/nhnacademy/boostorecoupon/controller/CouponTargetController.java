package com.nhnacademy.boostorecoupon.controller;

import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetSearchRequestDto;
import com.nhnacademy.boostorecoupon.service.CouponTargetService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupon-targets")
public class CouponTargetController {
    private final CouponTargetService couponTargetService;

    /**
     * 특정 쿠폰정책에 속하는 쿠폰대상 목록 조회
     * GET /api/coupon-targets?policy-id={policyId}&page={page}&pageSize={pageSize}
     * @param policyId : 쿠폰정책 식별키
     * @param page : 페이지 번호
     * @param pageSize : 페이지 크기
     * @return : Page<CouponTargetGetResponseDto>
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
