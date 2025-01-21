package com.nhnacademy.bookstorecoupon.controller;

import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryDuringRequestDto;
import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryFindRequestDto;
import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryResponseDto;
import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryStatusRequestDto;
import com.nhnacademy.bookstorecoupon.entity.CouponHistory;
import com.nhnacademy.bookstorecoupon.service.CouponHistoryService;
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
     * @param couponId : 쿠폰 ID
     * @param page : 페이지 번호
     * @param pageSize : 페이지 크기
     * @return : Page<CouponHistoryResponseDto> 쿠폰이력 ID, 쿠폰이력 상태, 변경일, 사유, 쿠폰 ID
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
     * @param status : 쿠폰이력 상태
     * @param page : 페이지 번호
     * @param pageSize : 페이지 크기
     * @return : Page<CouponHistoryResponseDto> 쿠폰이력 ID, 쿠폰이력 상태, 변경일, 사유, 쿠폰 ID
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
     * @param startDate : 시작일
     * @param endDate : 종료일
     * @param page : 페이지 번호
     * @param pageSize : 페이지 크기
     * @return : Page<CouponHistoryResponseDto> 쿠폰이력 ID, 쿠폰이력 상태, 변경일, 사유, 쿠폰 ID
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
