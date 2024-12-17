package com.nhnacademy.boostorenginx.config;


import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 NOT FOUND
    @ExceptionHandler({NotFoundCouponException.class, NotFoundCouponPolicyException.class, NotFoundMemberCouponException.class, NotFoundCouponHistoryException.class, NotFoundCouponTargetException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 400 BAD REQUEST
    @ExceptionHandler({CouponException.class, CouponPolicyException.class, MemberCouponException.class, CouponHistoryException.class, CouponTargetException.class})
    public ResponseEntity<String> handleCouponHistoryException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
