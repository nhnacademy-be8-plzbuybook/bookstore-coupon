package com.nhnacademy.boostorenginx.config;


import com.nhnacademy.boostorenginx.dto.error.ErrorResponseDto;
import com.nhnacademy.boostorenginx.error.*;
import com.nhnacademy.boostorenginx.skm.exception.KeyMangerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 NOT FOUND
    @ExceptionHandler({NotFoundCouponException.class, NotFoundCouponPolicyException.class, NotFoundMemberCouponException.class, NotFoundCouponHistoryException.class, NotFoundCouponTargetException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(RuntimeException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    // 400 BAD REQUEST
    @ExceptionHandler({CouponException.class, CouponPolicyException.class, MemberCouponException.class, CouponHistoryException.class, CouponTargetException.class, CouponCalculationExcption.class})
    public ResponseEntity<ErrorResponseDto> handleCouponHistoryException(RuntimeException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    //skm 예외
    @ExceptionHandler(KeyMangerException.class)
    public ResponseEntity<ErrorResponseDto> KeyManagerException(KeyMangerException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }
}
