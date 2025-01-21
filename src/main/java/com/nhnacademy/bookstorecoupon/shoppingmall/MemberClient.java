package com.nhnacademy.bookstorecoupon.shoppingmall;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "GATEWAY", contextId = "memberClient")
public interface MemberClient {

    // 이메일로 회원 식별키 조회
    @GetMapping("/api/members/id")
    ResponseEntity<Long> getMemberIdByEmail(@RequestHeader("X-USER-ID") String email);

}

