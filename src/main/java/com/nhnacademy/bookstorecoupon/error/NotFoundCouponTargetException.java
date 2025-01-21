package com.nhnacademy.bookstorecoupon.error;

public class NotFoundCouponTargetException extends RuntimeException {
    public NotFoundCouponTargetException(String message) {
        super(message);
    }
}
