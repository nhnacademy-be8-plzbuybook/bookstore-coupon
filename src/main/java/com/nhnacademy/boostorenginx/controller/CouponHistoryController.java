package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.couponhistory.*;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CouponHistoryController {
    private final CouponHistoryService couponHistoryService;

    // 쿠폰이력 조회 -> 예: GET /api/coupon-histories/1?page=1&size=3
    @GetMapping("/coupon-histories/{coupon-id}")
    public ResponseEntity<Page<CouponHistoryFindResponseDto>> getHistoryByCouponId(
            @PathVariable("coupon-id") Long couponId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        CouponHistoryFindRequestDto request = new CouponHistoryFindRequestDto(couponId, page, size);
        Page<CouponHistory> history = couponHistoryService.getHistoryByCouponId(request);
        Page<CouponHistoryFindResponseDto> response = history.map(CouponHistoryFindResponseDto::fromEntity);
        return ResponseEntity.ok(response);
    }

    // 특정 상태인 쿠폰이력 조회
    @GetMapping("/coupon-histories/status")
    public ResponseEntity<Page<CouponHistoryStatusResponseDto>> getHistoryByStatus(
            @RequestParam String status,
            @RequestParam int page,
            @RequestParam int size
    ) {
        CouponHistoryStatusRequestDto request = new CouponHistoryStatusRequestDto(status, page, size);
        Page<CouponHistory> history = couponHistoryService.getHistoryByStatus(request);
        Page<CouponHistoryStatusResponseDto> response = history.map(CouponHistoryStatusResponseDto::fromEntity);
        return ResponseEntity.ok(response);
    }

    // 특정 기간(활성화 기간인) 쿠폰이력 조회
    @GetMapping("/coupon-histories/active")
    public ResponseEntity<Page<CouponHistoryDuringResponseDto>> getHistoryDate(
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        CouponHistoryDuringRequestDto request = new CouponHistoryDuringRequestDto(start, end, page, size);
        Page<CouponHistory> history = couponHistoryService.getHistoryDate(request);
        Page<CouponHistoryDuringResponseDto> response = history.map(CouponHistoryDuringResponseDto::fromEntity);
        return ResponseEntity.ok(response);
    }

}
