package com.nhnacademy.bookstorecoupon.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponseDto {
    private int status;
    private String error;
    private String message;
}
