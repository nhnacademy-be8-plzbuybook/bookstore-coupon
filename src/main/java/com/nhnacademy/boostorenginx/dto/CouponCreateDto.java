package com.nhnacademy.boostorenginx.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class CouponCreateDto {
    private Long couponPolicyId; // 쿠폰정책 ID
    private LocalDateTime expiredAt; // 만료기간
}

/*
code 는 UUID 이므로 입력필요 x
초기 Status 는 UNUSED 로 고정되므로 클라이언가 입력 x
쿠폰발급시간은 서버가 쿠폰을 등록한 시점이므로 제외
 */