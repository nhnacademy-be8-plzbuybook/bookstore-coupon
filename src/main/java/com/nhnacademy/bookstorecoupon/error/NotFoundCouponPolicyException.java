package com.nhnacademy.bookstorecoupon.error;

public class NotFoundCouponPolicyException extends RuntimeException {
    public NotFoundCouponPolicyException(String message) {
        super(message);
    }
}
