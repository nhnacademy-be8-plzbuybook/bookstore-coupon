package com.nhnacademy.bookstorecoupon.error;

public class NotFoundCouponException extends RuntimeException {
    public NotFoundCouponException(String message) {
        super(message);
    }
}
