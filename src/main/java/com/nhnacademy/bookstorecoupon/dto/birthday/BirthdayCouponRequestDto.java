package com.nhnacademy.bookstorecoupon.dto.birthday;

import java.time.LocalDateTime;

public record BirthdayCouponRequestDto(
        Long memberId,
        LocalDateTime registerAt
) {
}
