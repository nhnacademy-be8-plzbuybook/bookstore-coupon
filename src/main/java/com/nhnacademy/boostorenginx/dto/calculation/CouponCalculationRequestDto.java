package com.nhnacademy.boostorenginx.dto.calculation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CouponCalculationRequestDto(
        @NotNull
        @DecimalMin("0.0")
        BigDecimal productPrice, // 주문상품 가격(개별)

        @NotNull
        @Min(1)
        Integer quantity // 주문상품 수량
) {
}
