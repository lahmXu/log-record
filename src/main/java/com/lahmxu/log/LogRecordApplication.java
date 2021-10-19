package com.lahmxu.log;

import com.lahmxu.log.annotation.EnableLogRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableLogRecord(tenant = "com.lahmxu.log")
public class LogRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogRecordApplication.class, args);
    }

}
