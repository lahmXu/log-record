package com.lahmxu.biz.controller;

import com.lahmxu.log.annotation.LogRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    @LogRecord(content = "修改了订单的配送员：从“{queryOldUser{#request.deliveryOrderNo()}}”," +
            " 修改到“{deveryUser{#request.userId}}”",bizNo = "#request.deliveryOrderNo")
    public void test() {

    }
}
