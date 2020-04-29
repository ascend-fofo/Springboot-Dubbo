package com.kgc.cn.consumer.controller;

import com.kgc.cn.consumer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/wxPay")
public class WxPayTestController {

    @Autowired
    private TestService testService;

    @GetMapping(value = "pay")
    public void toPay() {
        testService.isOrder();
    }
}
