package com.nhnacademy.boostorecoupon.service;

import com.nhnacademy.boostorecoupon.dto.welcome.WelcomeCouponRequestDto;

public interface WelcomeCouponService {

    void issueWelcomeCoupon(WelcomeCouponRequestDto requestDto); // Welcome 쿠폰 발급

}
