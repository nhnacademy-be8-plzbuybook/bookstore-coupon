package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupons")
public class CouponTargetController {
    private final CouponTargetService couponTargetService;

    // 쿠폰대상 생성
    @PostMapping("/targets")
    public ResponseEntity<CouponTargetResponseDto> createCouponTarget(@RequestBody CouponTargetAddRequestDto requestDto) {
        CouponTargetResponseDto couponTargetResponseDto = couponTargetService.createCouponTarget(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(couponTargetResponseDto);
    }

    // 쿠폰대상 조회
    @GetMapping("/targets/policy/{policyId}")
    public ResponseEntity<Page<CouponTargetGetResponseDto>> getCouponTarget(@PathVariable("policyId") Long policyId, Pageable pageable) {
        CouponTargetGetRequestDto requestDto = new CouponTargetGetRequestDto(policyId, pageable.getPageNumber(), pageable.getPageSize());
        Page<CouponTargetGetResponseDto> responseDto = couponTargetService.getCouponTargetsByPolicyId(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
