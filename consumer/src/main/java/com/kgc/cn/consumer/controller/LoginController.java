package com.kgc.cn.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kgc.cn.common.enums.ErrorEnums;
import com.kgc.cn.common.exception.ServiceException;
import com.kgc.cn.common.service.LoginService;
import com.kgc.cn.common.utils.RedisUtils;
import com.kgc.cn.common.vo.SupportUserLoginVo;
import com.kgc.cn.consumer.utils.ReturnResult;
import com.kgc.cn.consumer.utils.ReturnResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Log4j
@Api("登录控制层")
@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Reference
    private LoginService loginService;
    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation(value = "登录",notes = "248表示登录异常")
    @PostMapping(value = "toLogin")
    public ReturnResult toLogin(@Validated SupportUserLoginVo supportUserLoginVo, HttpServletRequest request){
        if (!ObjectUtils.isEmpty(supportUserLoginVo)) {
            boolean isToLogin = loginService.toLogin(supportUserLoginVo);
            if (isToLogin) {
                String token = request.getSession().getId();
                //保存到redis
                redisUtils.set(token,"用户对象");
                return ReturnResultUtils.returnSuccess(token);
            } else {
                return ReturnResultUtils.returnFail(248,"登录异常");
            }
        } else {
            throw new ServiceException(ErrorEnums.EMPTY_PARAM);
        }
    }

    @ApiOperation(value = "登出",notes = "101表示登出异常")
    @GetMapping(value = "/loginOut")
    public ReturnResult loginOut(@RequestParam("token") String token){
        try {
            redisUtils.del(token);
            return ReturnResultUtils.returnSuccess();
        } catch (Exception e) {
            log.error("redis删除" + token + "出现异常！");
            return ReturnResultUtils.returnFail(101,"loginOut Error!");
        }
    }
}
