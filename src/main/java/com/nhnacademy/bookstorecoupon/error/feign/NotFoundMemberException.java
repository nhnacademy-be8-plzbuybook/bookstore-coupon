package com.nhnacademy.bookstorecoupon.error.feign;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException(String message) {
        super(message);
    }
}
