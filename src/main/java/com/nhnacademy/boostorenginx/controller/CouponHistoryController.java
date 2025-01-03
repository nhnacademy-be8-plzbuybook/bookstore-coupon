package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryDuringRequestDto;
import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryFindRequestDto;
import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryResponseDto;
import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryStatusRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.service.CouponHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class CouponHistoryController {
    private final CouponHistoryService couponHistoryService;

    // 쿠폰 ID 에 해당하는 쿠폰이력 목록 조회
    @GetMapping("/histories")
    public ResponseEntity<Page<CouponHistoryResponseDto>> getHistoryByCouponId(@Valid CouponHistoryFindRequestDto couponHistoryFindRequestDto) {
        Page<CouponHistory> history = couponHistoryService.getHistoryByCouponId(couponHistoryFindRequestDto);
        Page<CouponHistoryResponseDto> response = history.map(CouponHistoryResponseDto::fromEntity);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 상태인 쿠폰이력 목록 조회
    @GetMapping("/histories/status")
    public ResponseEntity<Page<CouponHistoryResponseDto>> getHistoryByStatus(@Valid CouponHistoryStatusRequestDto couponHistoryStatusRequestDto) {
        Page<CouponHistory> history = couponHistoryService.getHistoryByStatus(couponHistoryStatusRequestDto);
        Page<CouponHistoryResponseDto> response = history.map(CouponHistoryResponseDto::fromEntity);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 기간 쿠폰이력 목록 조회
    @GetMapping("/histories/active")
    public ResponseEntity<Page<CouponHistoryResponseDto>> getHistoryDate(CouponHistoryDuringRequestDto couponHistoryDuringRequestDto) {
        Page<CouponHistory> history = couponHistoryService.getHistoryDate(couponHistoryDuringRequestDto);
        Page<CouponHistoryResponseDto> response = history.map(CouponHistoryResponseDto::fromEntity);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
