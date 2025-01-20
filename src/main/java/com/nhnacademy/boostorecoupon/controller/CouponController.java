package com.nhnacademy.boostorecoupon.controller;

import com.nhnacademy.boostorecoupon.dto.coupon.*;
import com.nhnacademy.boostorecoupon.service.CouponService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;

    /**
     * 쿠폰 생성
     * POST /api/coupons
     *
     * @param createRequest : 쿠폰정책 식별키, 쿠폰 만료일
     * @return : CouponCreateResponseDto 쿠폰 식별키
     */
    @PostMapping
    public ResponseEntity<CouponCreateResponseDto> createCoupon(@RequestBody @Valid CouponCreateRequestDto createRequest) {
        CouponResponseDto createdCoupon = couponService.createCoupon(createRequest);
        CouponCreateResponseDto response = new CouponCreateResponseDto(createdCoupon.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 모든 쿠폰 조회
     * GET /api/coupons
     *
     * @param page     페이지 번호
     * @param pageSize 페이지 크기
     * @return 쿠폰 목록 페이지네이션
     */
    @GetMapping
    public ResponseEntity<Page<CouponResponseDto>> getAllCoupons(@RequestParam @Min(0) int page, @RequestParam @Min(1) int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CouponResponseDto> coupons = couponService.getAllCoupons(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }

    /**
     * 쿠폰 ID 로 쿠폰 객체 조회
     * GET /api/coupons/id/{coupon-id}
     *
     * @param couponId : 쿠폰 식별키
     * @return : CouponResponseDto 쿠폰 ID, 코드, 상태, 생성일, 만료일, 쿠폰정책
     */
    @GetMapping("/id/{coupon-id}")
    public ResponseEntity<CouponResponseDto> getCouponById(@PathVariable("coupon-id") Long couponId) {
        CouponResponseDto couponResponseDto = couponService.findCouponById(couponId);

        return ResponseEntity.status(HttpStatus.OK).body(couponResponseDto);
    }

    /**
     * 특정 쿠폰정책에 속한 쿠폰 조회
     * GET /api/coupons/policies/{policy-id}
     * @param policyId : 쿠폰정책 ID
     * @param page : 페이지 번호
     * @param pageSize : 페이지 크기
     * @return : CouponResponseDto 쿠폰 ID, 코드, 상태, 생성일, 만료일, 쿠폰정책
     */
    @GetMapping("/policies/{policy-id}")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByPolicies(@PathVariable("policy-id") @Min(0) @NotNull Long policyId,
                                                                        @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                        @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        CouponFindCouponPolicyIdRequestDto couponFindCouponPolicyIdRequestDto = new CouponFindCouponPolicyIdRequestDto(policyId, page, pageSize);
        Page<CouponResponseDto> couponsByPolicy = couponService.getCouponsByPolicy(couponFindCouponPolicyIdRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponsByPolicy);
    }

    /**
     * 쿠폰 상태별 조회
     * GET /api/coupons/status
     * @param status : 쿠폰상태
     * @param page : 페이지 번호
     * @param pageSize : 페이지 크기
     * @return : CouponResponseDto 쿠폰 ID, 코드, 상태, 생성일, 만료일, 쿠폰정책
     */
    @GetMapping("/status")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByStatus(@RequestParam @NotNull String status, @Min(0) int page, @Min(1) int pageSize) {
        CouponFindStatusRequestDto couponFindStatusRequestDto = new CouponFindStatusRequestDto(status, page, pageSize);
        Page<CouponResponseDto> couponsByStatus = couponService.getCouponsByStatus(couponFindStatusRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponsByStatus);
    }

    /**
     * 쿠폰 사용으로 인한 상태변경 ( UNUSED - USED)
     * PATCH /api/coupons/{coupon-id}/use
     * @param couponId : 쿠폰 ID
     * @return : String 쿠폰 상태가 변경되었습니다
     */
    @PatchMapping("/{coupon-id}/use")
    public ResponseEntity<String> useCoupon(@PathVariable("coupon-id") @Min(0) Long couponId) {
        couponService.useCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).body("쿠폰 상태가 변경되었습니다");
    }


}
