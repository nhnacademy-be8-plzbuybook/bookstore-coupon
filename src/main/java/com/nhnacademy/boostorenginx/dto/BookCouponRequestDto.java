package com.nhnacademy.boostorenginx.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


// 특정 책 제목에 대한 쿠폰발급 요청 DTO
@Getter
@NoArgsConstructor
public class BookCouponRequestDto {
    private Long couponPolicyId; // 쿠폰 정책 ID
    private String bookTitle;    // 책 제목 -> 또는 책 ID
    private Boolean isStackable; // 중복 적용 여부
}
