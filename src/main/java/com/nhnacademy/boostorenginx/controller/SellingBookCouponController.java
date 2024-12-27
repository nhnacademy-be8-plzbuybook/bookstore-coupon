//package com.nhnacademy.boostorenginx.controller;
//
//import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
//import com.nhnacademy.boostorenginx.dto.sellingbook.SellingBookResponseDto;
//import com.nhnacademy.boostorenginx.service.SellingBookCouponService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RequestMapping("/api/")
//@RestController
//public class SellingBookCouponController {
//    private final SellingBookCouponService sellingBookCouponService;
//
//    // 특정 도서(판매책) 에 적용되는 쿠폰을 생성 -> 이게 맞나??
//    @PostMapping("/coupons/create")
//    public ResponseEntity<Long> createSellingBookCoupon(SellingBookResponseDto sellingBookResponseDto, @RequestBody CouponPolicySaveRequestDto saveRequestDto) {
//        Long couponId = sellingBookCouponService.createCouponForSellingBook(sellingBookResponseDto, saveRequestDto);
//        return ResponseEntity.ok(couponId);
//    }
//}
