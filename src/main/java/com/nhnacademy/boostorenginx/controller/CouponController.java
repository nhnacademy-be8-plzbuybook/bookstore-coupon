package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.service.CouponService;
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

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;

    /**
     * 쿠폰 ID 로 쿠폰 객체 조회
     * GET /api/coupons/id/{coupon-id}
     */
    @GetMapping("/id/{coupon-id}")
    public ResponseEntity<CouponResponseDto> getCouponById(@PathVariable("coupon-id") Long couponId) {
        CouponResponseDto couponResponseDto = couponService.findCouponById(couponId);

        return ResponseEntity.status(HttpStatus.OK).body(couponResponseDto);
    }

    /**
     * 쿠폰 생성
     * POST /api/coupons
     */
    @PostMapping
    public ResponseEntity<CouponCreateResponseDto> createCoupon(@RequestBody @Valid CouponCreateRequestDto createRequest) {
        CouponResponseDto createdCoupon = couponService.createCoupon(createRequest);
        CouponCreateResponseDto response = new CouponCreateResponseDto(createdCoupon.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 쿠폰 코드로 쿠폰 조회
     * GET /api/coupons/{coupon-code}
     */
    @GetMapping("{coupon-code}")
    public ResponseEntity<CouponCodeResponseDto> getCouponByCode(@PathVariable("coupon-code") @Valid String couponCode) {
        CouponCodeRequestDto couponCodeRequestDto = new CouponCodeRequestDto(couponCode);
        Coupon coupon = couponService.getCouponByCode(couponCodeRequestDto);

        CouponCodeResponseDto couponCodeResponseDto = CouponCodeResponseDto.fromCoupon(coupon);

        return ResponseEntity.status(HttpStatus.OK).body(couponCodeResponseDto);
    }

    /**
     * 만료된 쿠폰 조회
     * GET /api/coupons/expired
     */
    @GetMapping("/expired")
    public ResponseEntity<Page<CouponResponseDto>> getExpiredCoupons(@RequestParam("expiredAt") @NotNull LocalDateTime expiredAt,
                                                                     @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                     @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        CouponExpiredRequestDto couponExpiredRequestDto = new CouponExpiredRequestDto(expiredAt, page, pageSize);
        Page<CouponResponseDto> expiredCoupons = couponService.getExpiredCoupons(couponExpiredRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(expiredCoupons);
    }

    /**
     * 활성화된 쿠폰 조회
     * GET /api/coupons/active
     */
    @GetMapping("/active")
    public ResponseEntity<Page<CouponResponseDto>> getActiveCoupons(@RequestParam("currentDateTime") @NotNull LocalDateTime currentDateTime,
                                                                    @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                    @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        CouponActiveRequestDto couponActiveRequestDto = new CouponActiveRequestDto(currentDateTime, page, pageSize);
        Page<CouponResponseDto> activeCoupons = couponService.getActiveCoupons(couponActiveRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(activeCoupons);
    }

    /**
     * 특정 쿠폰정책에 속한 쿠폰 조회
     * GET /api/coupons/policies/{policy-id}
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
     */
    @GetMapping("/status")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByStatus(@RequestParam @NotNull String status, @Min(0) int page, @Min(1) int pageSize) {
        CouponFindStatusRequestDto couponFindStatusRequestDto = new CouponFindStatusRequestDto(status, page, pageSize);
        Page<CouponResponseDto> couponsByStatus = couponService.getCouponsByStatus(couponFindStatusRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponsByStatus);
    }

    /**
     * 만료된 쿠폰 상태 변경
     * PATCH /api/coupons/expired
     */
    @PatchMapping("/expired")
    public ResponseEntity<Page<CouponResponseDto>> updateExpiredCoupons(@RequestBody @Valid CouponUpdateExpiredRequestDto request) {
        couponService.updateExpiredCoupon(request);
        CouponExpiredRequestDto couponExpiredRequestDto = new CouponExpiredRequestDto(request.expiredDate(), request.page(), request.size());
        Page<CouponResponseDto> expiredCoupons = couponService.getExpiredCoupons(couponExpiredRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(expiredCoupons);
    }

    /**
     * 쿠폰 상태 변경: UNUSED -> USED
     * PATCH /api/coupons/{coupon-id}/use
     */
    @PatchMapping("/{coupon-id}/use")
    public ResponseEntity<String> useCoupon(@PathVariable("coupon-id") @Min(0) Long couponId) {
        couponService.useCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).body("쿠폰 상태가 변경되었습니다");
    }

    /**
     * 모든 쿠폰 조회
     * GET /api/coupons
     * @param page     페이지 번호 (최소 0)
     * @param pageSize 페이지 크기 (최소 1)
     * @return 쿠폰 목록 페이지네이션
     */
    @GetMapping
    public ResponseEntity<Page<CouponResponseDto>> getAllCoupons(@RequestParam @Min(0) int page, @RequestParam @Min(1)int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CouponResponseDto> coupons = couponService.getAllCoupons(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(coupons);
    }
}
