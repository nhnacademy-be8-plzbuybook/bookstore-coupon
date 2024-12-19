package com.nhnacademy.boostorenginx.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseConnectionChecker implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("Connected to database: " + dataSource.getConnection().getMetaData().getURL());
        } catch (Exception e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
    }
}