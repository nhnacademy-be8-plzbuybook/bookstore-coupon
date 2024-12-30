package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.service.SellingBookCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/coupons")
@RestController
public class SellingBookCouponController {
    private final SellingBookCouponService sellingBookCouponService;

    @PostMapping("/selling-books/{searchKeyword}")
    public ResponseEntity<String> createSellingBookCoupon(@PathVariable String searchKeyword, @RequestBody CouponPolicySaveRequestDto couponPolicySaveRequestDto) {
        sellingBookCouponService.createCouponForSellingBook(searchKeyword, couponPolicySaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("도서 쿠폰이 성공적으로 발급되었습니다");
    }
}
