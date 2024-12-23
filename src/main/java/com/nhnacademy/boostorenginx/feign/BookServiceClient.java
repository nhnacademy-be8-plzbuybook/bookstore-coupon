//package com.nhnacademy.boostorenginx.feign;
//
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//import java.awt.print.Book;
//
//@FeignClient(name = "http://localhost:8761/eureka/")
//public interface BookServiceClient {
//
//    @GetMapping("연결할서비스매핑HTTP요청")
//    Book getBookById(@PathVariable("id") Long id); // 예시: 책 ID 요청 페인클라이언트
//
//    @PostMapping("/books")
//    Book createBook(@RequestBody Book book, @RequestHeader("Authorization") String token); // 예시: 책 생성 요청 페인클라이언트, 헤더설정가능
//
//    //
//}
//
// // 메시지 큐 ~ or 페인클라이언트 생각해볼것