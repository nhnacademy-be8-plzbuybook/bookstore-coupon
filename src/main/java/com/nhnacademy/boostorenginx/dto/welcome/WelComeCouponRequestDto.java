package com.nhnacademy.boostorenginx.dto.welcome;

import java.time.LocalDateTime;

public record WelComeCouponRequestDto(
        Long memberId,
        LocalDateTime registeredAt
) {

}
