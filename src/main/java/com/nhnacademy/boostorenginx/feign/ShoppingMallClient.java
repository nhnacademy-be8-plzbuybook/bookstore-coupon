package com.nhnacademy.boostorenginx.feign;

import com.nhnacademy.boostorenginx.dto.category.CategorySimpleResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bookstore")
public interface ShoppingMallClient {

    // 쇼핑몰 서버에 카테고리 정보요청
    @GetMapping("/api/categories/search")
    List<CategorySimpleResponseDto> searchCategories(@RequestParam String keyword);
}

