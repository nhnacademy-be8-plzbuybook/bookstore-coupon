package com.nhnacademy.boostorenginx.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "bookstore")
public interface ShoppingMallClient {



}

