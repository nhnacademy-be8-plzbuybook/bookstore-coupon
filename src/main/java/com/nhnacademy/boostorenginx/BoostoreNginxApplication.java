package com.nhnacademy.boostorenginx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // 페인클라이언트
public class BoostoreNginxApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoostoreNginxApplication.class, args);
    }

}
