package com.nhnacademy.boostorecoupon.controller;

import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryDuringRequestDto;
import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryFindRequestDto;
import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryResponseDto;
import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryStatusRequestDto;
import com.nhnacademy.boostorecoupon.entity.CouponHistory;
import com.nhnacademy.boostorecoupon.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api/coupon-histories")
@RestController
public class CouponHistoryController {
    private final CouponHistoryService couponHistoryService;

    /**
     * 쿠폰 ID에 해당하는 쿠폰 이력 목록 조회
     * GET /api/coupon-histories/{coupon-id}
     */
    @GetMapping("/{coupon-id}")
    public ResponseEntity<Page<CouponHistoryResponseDto>> getHistoryByCouponId(@PathVariable("coupon-id") Long couponId,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int pageSize) {
        CouponHistoryFindRequestDto couponHistoryFindRequestDto = new CouponHistoryFindRequestDto(couponId, page, pageSize);
        Page<CouponHistory> history = couponHistoryService.getHistoryByCouponId(couponHistoryFindRequestDto);
        Page<CouponHistoryResponseDto> response = history.map(CouponHistoryResponseDto::fromEntity);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 특정 상태의 쿠폰 이력 목록 조회
     * GET /api/coupon-histories/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<CouponHistoryResponseDto>> getHistoryByStatus(@PathVariable String status,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int pageSize) {
        CouponHistoryStatusRequestDto couponHistoryStatusRequestDto = new CouponHistoryStatusRequestDto(status, page, pageSize);
        Page<CouponHistory> history = couponHistoryService.getHistoryByStatus(couponHistoryStatusRequestDto);
        Page<CouponHistoryResponseDto> response = history.map(CouponHistoryResponseDto::fromEntity);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 특정 기간의 쿠폰 이력 목록 조회
     * GET /api/coupon-histories/period
     */
    @GetMapping("/period")
    public ResponseEntity<Page<CouponHistoryResponseDto>> getHistoryDate(@RequestParam("start-date") String startDate,
                                                                         @RequestParam("end-date") String endDate,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int pageSize) {
        CouponHistoryDuringRequestDto couponHistoryDuringRequestDto = new CouponHistoryDuringRequestDto(
                LocalDateTime.parse(startDate),
                LocalDateTime.parse(endDate),
                page,
                pageSize
        );
        Page<CouponHistory> history = couponHistoryService.getHistoryDate(couponHistoryDuringRequestDto);
        Page<CouponHistoryResponseDto> response = history.map(CouponHistoryResponseDto::fromEntity);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
