package com.example.log;

import com.lahmxu.log.starter.annotation.EnableLogRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableLogRecord(tenant = "com.example.log")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LogExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogExampleApplication.class, args);
    }

}
