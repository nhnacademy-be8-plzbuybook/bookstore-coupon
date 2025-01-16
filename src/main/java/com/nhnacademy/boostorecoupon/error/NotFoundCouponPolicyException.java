package com.nhnacademy.boostorecoupon.error;

public class NotFoundCouponPolicyException extends RuntimeException {
    public NotFoundCouponPolicyException(String message) {
        super(message);
    }
}
