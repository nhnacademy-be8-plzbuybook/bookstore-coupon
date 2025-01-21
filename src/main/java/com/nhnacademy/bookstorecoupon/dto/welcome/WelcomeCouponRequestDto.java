package com.nhnacademy.bookstorecoupon.dto.welcome;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record WelcomeCouponRequestDto(
        @NotNull
        Long memberId,
        @NotNull
        LocalDateTime registeredAt
) {
}
