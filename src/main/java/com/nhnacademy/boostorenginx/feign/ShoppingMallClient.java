package com.nhnacademy.boostorenginx.feign;

import com.nhnacademy.boostorenginx.dto.category.CategorySimpleResponseDto;
import com.nhnacademy.boostorenginx.dto.sellingbook.BookDetailResponseDto;
import com.nhnacademy.boostorenginx.dto.sellingbook.BookSearchResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bookstore")
public interface ShoppingMallClient {

    // 쇼핑몰 서버에 카테고리 정보요청
    @GetMapping("/api/categories/search")
    List<CategorySimpleResponseDto> searchCategories(@RequestParam String keyword);

    // 쇼핑몰 서버에 특정 도서의 정보를 요청
    @GetMapping("/api/selling-books/{sellingBookId}")
    BookDetailResponseDto getSellingBook(@PathVariable Long sellingBookId);

    // 쇼핑몰 서버에서 키워드로 검색(책 이름, 작가, 카테고리 등)
    @GetMapping("/api/books")
    List<BookSearchResponseDto> searchBooks(@RequestParam String searchKeyword);

}

