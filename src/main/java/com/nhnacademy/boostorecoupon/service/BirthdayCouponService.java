package com.nhnacademy.boostorecoupon.service;

import com.nhnacademy.boostorecoupon.dto.birthday.BirthdayCouponRequestDto;

public interface BirthdayCouponService {

    void issueBirthdayCoupon(BirthdayCouponRequestDto requestDto); // 생일쿠폰 발급

}
