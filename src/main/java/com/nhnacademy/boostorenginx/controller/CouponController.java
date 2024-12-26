package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CouponController {
    private final CouponService couponService;

    // 쿠폰생성
    @PostMapping("/coupons")
    public ResponseEntity<CouponCreateResponseDto> createCoupon(@RequestBody CouponCreateRequestDto createRequest) {
        CouponResponseDto createdCoupon = couponService.createCoupon(createRequest);
        CouponCreateResponseDto response = new CouponCreateResponseDto(createdCoupon.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 쿠폰코드로 검색
    @GetMapping("/coupons/code/{code}")
    public ResponseEntity<CouponCodeResponseDto> getCouponByCode(@PathVariable("code") String code) {
        CouponCodeRequestDto requestDto = new CouponCodeRequestDto(code);
        Coupon coupon = couponService.getCouponByCode(requestDto);
        CouponCodeResponseDto couponCodeResponseDto = CouponCodeResponseDto.fromCoupon(coupon);
        return ResponseEntity.status(HttpStatus.OK).body(couponCodeResponseDto);
    }

    // 만료된 쿠폰들 검색 -> 예: GET /api/coupons/expired?expiredAt=2023-12-31T23:59:59&page=0&pageSize=10
    @GetMapping("/coupons/expired")
    public ResponseEntity<Page<CouponResponseDto>> getExpiredCoupons(@RequestParam("expiredAt") LocalDateTime expiredAt,
                                                                            @RequestParam("page") int page,
                                                                            @RequestParam("pageSize") int pageSize) {
        CouponExpiredRequestDto couponExpiredRequestDto = new CouponExpiredRequestDto(expiredAt, page, pageSize);

        Page<CouponResponseDto> expiredCoupons = couponService.getExpiredCoupons(couponExpiredRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(expiredCoupons);
    }

    // 활성화된 쿠폰들 검색 -> 예: GET /api/coupons/active?currentDateTime=2023-12-20T12:00:00&page=1&pageSize=3
    @GetMapping("/coupons/active")
    public ResponseEntity<Page<CouponResponseDto>> getActiveCoupons(@RequestParam("currentDateTime") LocalDateTime currentDateTime,
                                                                          @RequestParam("page") int page,
                                                                          @RequestParam("pageSize") int pageSize) {
        CouponActiveRequestDto requestDto = new CouponActiveRequestDto(currentDateTime, page, pageSize);

        Page<CouponResponseDto> activeCoupons = couponService.getActiveCoupons(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(activeCoupons);
    }

    // 쿠폰정책들로 검색 -> 예: GET /api/coupons/by-policy/123?page=0&pageSize=10
    @GetMapping("/coupons/coupon-policies/{policy-id}")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByPolicies(@PathVariable("policy-id") Long policyId,
                                                                                          @RequestParam("page") int page,
                                                                                          @RequestParam("pageSize") int pageSize) {
        CouponFindCouponPolicyIdRequestDto requestDto = new CouponFindCouponPolicyIdRequestDto(policyId, page, pageSize);

        Page<CouponResponseDto> couponsByPolicy = couponService.getCouponsByPolicy(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponsByPolicy);
    }

    // 쿠폰 상태로 검색 -> 예: GET /api/coupons/status?status=UNUSED&page=0&pageSize=5
    @GetMapping("/coupons/status")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByStatus(
            @RequestParam("status") String status,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize
    ) {
        CouponFindStatusRequestDto requestDto = new CouponFindStatusRequestDto(status, page, pageSize);

        Page<CouponResponseDto> couponsByStatus = couponService.getCouponsByStatus(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponsByStatus);
    }

    // 만료되었는지 체크 -> 쿠폰 상태를 Expired 로 변경
    @PatchMapping("/coupons/update-expired")
    public ResponseEntity<Page<CouponResponseDto>> updateExpiredCoupons(@RequestBody CouponUpdateExpiredRequestDto request) {
        couponService.updateExpiredCoupon(request);
        Page<CouponResponseDto> expiredCoupons = couponService.getExpiredCoupons(
                new CouponExpiredRequestDto(request.expiredDate(), request.page(), request.size())
        );
        return ResponseEntity.status(HttpStatus.OK).body(expiredCoupons);
    }

    // 쿠폰 사용 -> 쿠폰 상태를 USED 로 변경
    @PatchMapping("/coupons/use")
    public ResponseEntity<Map<String, String>> useCoupon(@RequestBody MemberCouponUseRequestDto request) {
        couponService.useCoupon(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "쿠폰이 성공적으로 사용되었습니다");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
