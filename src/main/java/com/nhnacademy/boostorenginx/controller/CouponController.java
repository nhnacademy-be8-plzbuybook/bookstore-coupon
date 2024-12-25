package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.error.NotFoundCouponException;
import com.nhnacademy.boostorenginx.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CouponController {
    private final CouponService couponService;

    // 쿠폰생성
    @PostMapping("/coupons")
    public ResponseEntity<CouponCreateResponseDto> createCoupon(@RequestBody CouponCreateRequestDto createRequest) {
        Long couponId = couponService.createCoupon(createRequest);
        CouponCreateResponseDto response = new CouponCreateResponseDto(couponId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 쿠폰코드로 검색
    @GetMapping("/coupons/{code}")
    public ResponseEntity<CouponCodeResponseDto> getCouponByCode(@PathVariable("code") String code) {
        CouponCodeRequestDto request = new CouponCodeRequestDto(code);
        Coupon coupon = couponService.getCouponByCode(request).orElseThrow(
                () -> new NotFoundCouponException("입력받은 code 에 해당하는 쿠폰이 없습니다: " + code)
        );
        CouponCodeResponseDto response = CouponCodeResponseDto.fromEntity(coupon);
        return ResponseEntity.ok(response);
    }

    // 만료된 쿠폰들 검색 -> 예: GET /api/coupons/expired?expiredAt=2023-12-31T23:59:59&page=0&pageSize=10
    @GetMapping("/coupons/expired")
    public ResponseEntity<Page<CouponExpiredResponseDto>> getExpiredCoupons(@RequestParam("expiredAt") LocalDateTime expiredAt,
                                                                            @RequestParam("page") int page,
                                                                            @RequestParam("pageSize") int pageSize) {
        CouponExpiredRequestDto request = new CouponExpiredRequestDto(expiredAt, page, pageSize);
        Page<Coupon> expiredCoupons = couponService.getExpiredCoupons(request);
        Page<CouponExpiredResponseDto> response = expiredCoupons.map(CouponExpiredResponseDto::fromEntity);
        return ResponseEntity.ok(response);
    }

    // 활성화된 쿠폰들 검색 -> 예: GET /api/coupons/active?currentDateTime=2023-12-20T12:00:00&page=1&pageSize=3
    @GetMapping("/coupons/active")
    public ResponseEntity<Page<CouponActiveResponseDto>> getActiveCoupons(@RequestParam("currentDateTime") LocalDateTime currentDateTime,
                                                                          @RequestParam("page") int page,
                                                                          @RequestParam("pageSize") int pageSize) {
        CouponActiveRequestDto request = new CouponActiveRequestDto(currentDateTime, page, pageSize);
        Page<Coupon> coupons = couponService.getActiveCoupons(request);
        Page<CouponActiveResponseDto> response = coupons.map(CouponActiveResponseDto::fromEntity);
        return ResponseEntity.ok(response);
    }

    // 쿠폰정책들로 검색 -> 예: GET /api/coupons/by-policy/123?page=0&pageSize=10
    @GetMapping("/coupons/coupon-policies/{policy-id}")
    public ResponseEntity<Page<CouponFindCouponPolicyIdResponseDto>> getCouponsByPolicies(@PathVariable("policy-id") Long policyId,
                                                                                          @RequestParam("page") int page,
                                                                                          @RequestParam("pageSize") int pageSize) {
        CouponFindCouponPolicyIdRequestDto request = new CouponFindCouponPolicyIdRequestDto(policyId, page, pageSize);
        Page<Coupon> coupons = couponService.getCouponsByPolicy(request);
        Page<CouponFindCouponPolicyIdResponseDto> response = coupons.map(CouponFindCouponPolicyIdResponseDto::fromEntity);
        return ResponseEntity.ok(response);
    }

    // 쿠폰 상태로 검색 -> 예: GET /api/coupons/status?status=UNUSED&page=0&pageSize=5
    @GetMapping("/coupons/status")
    public ResponseEntity<Page<CouponFindStatusResponseDto>> getCouponsByStatus(
            @RequestParam("status") String status,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize
    ) {
        CouponFindStatusRequestDto requestDto = new CouponFindStatusRequestDto(status, page, pageSize);
        Page<Coupon> coupons = couponService.getCouponsByStatus(requestDto);
        Page<CouponFindStatusResponseDto> response = coupons.map(CouponFindStatusResponseDto::fromEntity);
        return ResponseEntity.ok(response);
    }

    // 만료되었는지 체크 -> 쿠폰 상태를 Expired 로 변경
    @PatchMapping("/coupons/update-expired")
    public ResponseEntity<String> updateExpiredCoupons(@RequestBody CouponUpdateExpiredRequestDto request) {
        couponService.updateExpiredCoupon(request);
        return ResponseEntity.ok("쿠폰을 만료된 상태로 업데이트 하였습니다");
    }

    // 쿠폰 사용 -> 쿠폰 상태를 USED 로 변경
    @PatchMapping("/coupons/use")
    public ResponseEntity<String> useCoupon(@RequestBody MemberCouponUseRequestDto request) {
        couponService.useCoupon(request);
        return ResponseEntity.ok("쿠폰 사용완료");
    }
}
