package com.nhnacademy.boostorenginx.feign;

import com.nhnacademy.boostorenginx.dto.sellingbook.SellingBookResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "bookstore") // 게이트웨이 url 도는 유레카
public interface ShoppingMallClient {



}

