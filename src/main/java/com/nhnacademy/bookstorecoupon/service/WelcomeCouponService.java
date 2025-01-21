package com.nhnacademy.bookstorecoupon.service;

import com.nhnacademy.bookstorecoupon.dto.welcome.WelcomeCouponRequestDto;

public interface WelcomeCouponService {

    void issueWelcomeCoupon(WelcomeCouponRequestDto requestDto); // Welcome 쿠폰 발급

}
