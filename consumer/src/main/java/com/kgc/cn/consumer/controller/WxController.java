package com.kgc.cn.consumer.controller;

import com.alibaba.fastjson.JSONObject;
import com.kgc.cn.common.utils.HttpClientUtils;
import com.kgc.cn.common.utils.RedisUtils;
import com.kgc.cn.consumer.wx.WxConfig;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


@Controller
@RequestMapping(value = "/wx")
public class WxController {
    @Autowired
    private WxConfig wxConfig;
    @Reference
    private RedisUtils redisUtils;

    @GetMapping(value = "wxLogin")
    public String wxLogin(){
        String codeUri = wxConfig.reqCodeUri();
        return "redirect:" + codeUri;
    }

    @GetMapping(value = "callBack")
    public String callBack(String code, HttpServletRequest request) throws IOException {
        if(null != code) {
            String accessTokenStr = HttpClientUtils.doGet(wxConfig.reqAccessTokenUri(code));
            JSONObject jsonObject = JSONObject.parseObject(accessTokenStr);
            String accessToken = jsonObject.getString("access_token");
            String openId = jsonObject.getString("openid");
            String userInfo = HttpClientUtils.doGet(wxConfig.reqUserInfoUri(accessToken,openId));
            redisUtils.set(openId,userInfo);
            return "redirect:" + wxConfig.getLoginSuccess() + openId;
        }
        return null;
    }

}
