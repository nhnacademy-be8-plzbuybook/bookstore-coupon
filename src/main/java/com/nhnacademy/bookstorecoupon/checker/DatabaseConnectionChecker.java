package com.nhnacademy.bookstorecoupon.checker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseConnectionChecker implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Connected to database: {}", dataSource.getConnection().getMetaData().getURL());
        } catch (Exception e) {
            log.info("Failed to connect to database: {}", e.getMessage());
        }
    }
}