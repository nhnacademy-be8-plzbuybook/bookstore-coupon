package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService couponService;

    // 쿠폰생성
    @PostMapping
    public ResponseEntity<CouponCreateResponseDto> createCoupon(@RequestBody @Valid CouponCreateRequestDto createRequest) {
        CouponResponseDto createdCoupon = couponService.createCoupon(createRequest);
        CouponCreateResponseDto response = new CouponCreateResponseDto(createdCoupon.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 쿠폰코드로 검색
    @GetMapping("/code")
    public ResponseEntity<CouponCodeResponseDto> getCouponByCode(@Valid CouponCodeRequestDto couponCodeRequestDto) {
        Coupon coupon = couponService.getCouponByCode(couponCodeRequestDto);
        CouponCodeResponseDto couponCodeResponseDto = CouponCodeResponseDto.fromCoupon(coupon);

        return ResponseEntity.status(HttpStatus.OK).body(couponCodeResponseDto);
    }

    // 만료된 쿠폰들 검색
    @GetMapping("/expired")
    public ResponseEntity<Page<CouponResponseDto>> getExpiredCoupons(@Valid CouponExpiredRequestDto couponExpiredRequestDto) {
        Page<CouponResponseDto> expiredCoupons = couponService.getExpiredCoupons(couponExpiredRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(expiredCoupons);
    }

    // 활성화된 쿠폰들 검색
    @GetMapping("/active")
    public ResponseEntity<Page<CouponResponseDto>> getActiveCoupons(@Valid CouponActiveRequestDto couponActiveRequestDto) {
        Page<CouponResponseDto> activeCoupons = couponService.getActiveCoupons(couponActiveRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(activeCoupons);
    }

    // 쿠폰정책들로 검색
    @GetMapping("/coupon-policies")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByPolicies(@Valid CouponFindCouponPolicyIdRequestDto couponFindCouponPolicyIdRequestDto) {
        Page<CouponResponseDto> couponsByPolicy = couponService.getCouponsByPolicy(couponFindCouponPolicyIdRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponsByPolicy);
    }

    // 쿠폰 상태로 검색
    @GetMapping("/status")
    public ResponseEntity<Page<CouponResponseDto>> getCouponsByStatus(@Valid CouponFindStatusRequestDto couponFindStatusRequestDto) {
        Page<CouponResponseDto> couponsByStatus = couponService.getCouponsByStatus(couponFindStatusRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(couponsByStatus);
    }

    // 만료된 쿠폰의 상태를 Expired 로 변경
    @PatchMapping("/update-expired")
    public ResponseEntity<Page<CouponResponseDto>> updateExpiredCoupons(@RequestBody @Valid CouponUpdateExpiredRequestDto request) {
        couponService.updateExpiredCoupon(request);
        Page<CouponResponseDto> expiredCoupons = couponService.getExpiredCoupons(
                new CouponExpiredRequestDto(request.expiredDate(), request.page(), request.size())
        );
        return ResponseEntity.status(HttpStatus.OK).body(expiredCoupons);
    }

    // 사용가능한 쿠폰 상태(UNUSED)를 USED 로 변경
    @PatchMapping("/use")
    public ResponseEntity<Map<String, String>> useCoupon(@RequestBody @Valid MemberCouponUseRequestDto request) {
        couponService.useCoupon(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "쿠폰이 성공적으로 사용되었습니다");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
