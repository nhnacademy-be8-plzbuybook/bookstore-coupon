package com.nhnacademy.bookstorecoupon.scheduler;

import com.nhnacademy.bookstorecoupon.service.ExpiredCheckService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExpiredCheckSchedulerTest {

    @InjectMocks
    private ExpiredCheckScheduler expiredCheckScheduler;

    @Mock
    private ExpiredCheckService expiredCheckService;

    @DisplayName("만료된 쿠폰 체크 스케줄러 작동")
    @Test
    void expiredCheck() {
        expiredCheckScheduler.expiredCheck();
        verify(expiredCheckService, times(1)).checkExpiredCoupon();
    }
}