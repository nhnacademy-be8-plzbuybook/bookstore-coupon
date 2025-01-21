package com.nhnacademy.bookstorecoupon.error;

public class NotFoundMemberCouponException extends RuntimeException {
    public NotFoundMemberCouponException(String message) {
        super(message);
    }
}
