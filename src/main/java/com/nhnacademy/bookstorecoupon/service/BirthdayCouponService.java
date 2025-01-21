package com.nhnacademy.bookstorecoupon.service;

import com.nhnacademy.bookstorecoupon.dto.birthday.BirthdayCouponRequestDto;

public interface BirthdayCouponService {

    void issueBirthdayCoupon(BirthdayCouponRequestDto requestDto); // 생일쿠폰 발급

}
