package com.nhnacademy.boostorecoupon.error;

public class NotFoundCouponException extends RuntimeException {
    public NotFoundCouponException(String message) {
        super(message);
    }
}
