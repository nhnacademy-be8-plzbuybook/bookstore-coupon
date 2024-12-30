package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.dto.sellingbook.BookSearchResponseDto;
import com.nhnacademy.boostorenginx.feign.ShoppingMallClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class SellingBookCouponService {
    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;
    private final CouponService couponService;

    private final ShoppingMallClient shoppingMallClient;

    public void createCouponForSellingBook(String searchKeyword, CouponPolicySaveRequestDto couponPolicySaveRequestDto) {
        // 쇼핑몰 서버에서 키워드로 책 검색
        List<BookSearchResponseDto> books = shoppingMallClient.searchBooks(searchKeyword);

        // 쿠폰정책 생성
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);
        Long couponPolicyId = couponPolicyResponseDto.id();

        for (BookSearchResponseDto book : books) {
            // 판매책별 쿠폰대상 생성
            CouponTargetAddRequestDto couponTargetAddRequestDto = new CouponTargetAddRequestDto(couponPolicyId, book.getSellingBookId());
            CouponTargetResponseDto couponTargetResponseDto = couponTargetService.createCouponTarget(couponTargetAddRequestDto);

            // 판매책별 쿠폰 생성
            CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(couponPolicyId, LocalDateTime.now());
            CouponResponseDto couponResponseDto = couponService.createCoupon(couponCreateRequestDto);
            log.info("카테고리 쿠폰 발급 완료 CouponId = {}", couponResponseDto.id());
        }
    }
}
