package com.kgc.cn.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.kgc.cn.common.service.DubboTestService;
import com.kgc.cn.common.vo.UserVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dubbox")
public class DubboTestController {

    @Reference
    private DubboTestService dubboTestService;

    @GetMapping(value = "/testdubbo")
    public int testDubbo() {
        try {
            dubboTestService.testDubbo();
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    @GetMapping(value = "/getUser")
    public UserVo getUser() {
        return dubboTestService.getUser();
    }


}
