package com.nhnacademy.boostorenginx.feign;

import com.nhnacademy.boostorenginx.dto.sellingbook.SellingBookResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "bookstore-back1") // 게이트웨이 url 도는 유레카
public interface ShoppingMallClient {

    @GetMapping("/api/selling-books/{sellingBookId}")
    ResponseEntity<SellingBookResponseDto> getBookById(@PathVariable Long sellingBookId);
    // ResponseEntity 로

//    @PostMapping("/books")
//    Book createBook(@RequestBody Book book, @RequestHeader("Authorization") String token); // 예시: 책 생성 요청 페인클라이언트, 헤더설정가능


}

