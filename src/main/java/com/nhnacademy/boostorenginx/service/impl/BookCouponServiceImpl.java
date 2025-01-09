package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.feign.ShoppingMallClient;
import com.nhnacademy.boostorenginx.service.BookCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BookCouponServiceImpl implements BookCouponService {
    private ShoppingMallClient shoppingMallClient;

    // 쿠폰정책 생성할때 scope 가 BOOK 인 경우 실행 -> 쇼핑몰 서버의 책검색 사용 -> 반환 책 판매 id 들을 모두 쿠폰대상에 추가 -> 쿠폰정책 생성 -> 쿠폰 생성 까지

}
