package com.nhnacademy.boostorenginx.error;

public class NotFoundCouponException extends RuntimeException {
    public NotFoundCouponException(String message) {
        super(message);
    }
}
