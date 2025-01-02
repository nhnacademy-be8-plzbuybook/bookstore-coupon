package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<CouponTargetResponseDto> createCouponTarget(@RequestBody @Valid CouponTargetAddRequestDto requestDto) {
        CouponTargetResponseDto couponTargetResponseDto = couponTargetService.createCouponTarget(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(couponTargetResponseDto);
    }

    // 특정 쿠폰정책 에 속하는 쿠폰대상 목록 조회
    @GetMapping("/targets/policy")
    public ResponseEntity<Page<CouponTargetGetResponseDto>> getCouponTarget(@Valid CouponTargetGetRequestDto couponTargetGetRequestDto) {
        Page<CouponTargetGetResponseDto> responseDto = couponTargetService.getCouponTargetsByPolicyId(couponTargetGetRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
