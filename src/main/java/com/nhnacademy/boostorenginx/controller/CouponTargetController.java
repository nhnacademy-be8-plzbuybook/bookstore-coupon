package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CouponTargetController {
    private final CouponTargetService couponTargetService;

    // 쿠폰대상 생성
    @PostMapping("/coupon-targets")
    public ResponseEntity<CouponTargetResponseDto> createCouponTarget(@RequestBody CouponTargetAddRequestDto requestDto) {
        CouponTargetResponseDto couponTargetResponseDto = couponTargetService.createCouponTarget(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(couponTargetResponseDto);
    }

    // 페인클라이언트 - 판매책 쿠폰대상 조회
//    @GetMapping("/selling-books/{sellingBookId}")
//    public ResponseEntity<SellingBookResponseDto> getSellingBook(@PathVariable("sellingBookId") Long sellingBookId) {
//        return sellingBookClient.getBookById(sellingBookId);
//    }

}
