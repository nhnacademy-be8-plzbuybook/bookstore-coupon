package com.nhnacademy.boostorecoupon.scheduler;

import com.nhnacademy.boostorecoupon.service.ExpiredCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExpiredCheckScheduler {
    private final ExpiredCheckService expiredCheckService;

    // 매일 00:00 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void expiredCheck() {
        log.info("스케줄러 실행 시작");
        expiredCheckService.checkExpiredCoupon();
        log.info("스케줄러 실행 종료");
    }
}
