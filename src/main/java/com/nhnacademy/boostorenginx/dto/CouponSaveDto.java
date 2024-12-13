package com.nhnacademy.boostorenginx.dto;

import com.nhnacademy.boostorenginx.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CouponSaveDto {
    private String code;
    private Status status;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
}
