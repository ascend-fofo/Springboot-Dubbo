package com.kgc.cn.consumer.controller;

import com.kgc.cn.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "/redis")
public class HRedisController {
    @Autowired
    private RedisUtils redisUtils;

    String nameSpace = "TEAM_PURCHASE";

    @GetMapping(value = "/setKeys")
    public void setKeys(String str){
        redisUtils.set(nameSpace + str,1);
    }

    @GetMapping(value = "/getKeys")
    public Set<String> getKeys(){
        return redisUtils.keys(nameSpace);
    }
}
