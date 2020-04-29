package com.kgc.cn.consumer.conf;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.kgc.cn.common.dto.LoginUser;
import com.kgc.cn.common.utils.RedisUtils;
import com.kgc.cn.consumer.wx.WxConfig;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class LoginReqComplete implements HandlerInterceptor {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private WxConfig wxConfig;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果方法上没有添加@LoginRequired注解就不需要登录可以访问的接口，也就意味着不要进入到这个方法中来
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断接口是否需要登录
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
        if (null != methodAnnotation) {
            String token = request.getHeader("token");
            String wxToken = request.getHeader("wxToken");
            String headerToken = token == null ? wxToken : null;
            if (StringUtils.isNotEmpty(headerToken)) {
                String userToken = (String)redisUtils.get(headerToken);
                //Object wxToken = redisUtils.get(wxConfig.getAppId());
                if (StringUtils.isEmpty(userToken)) {
                    throw new RuntimeException("Login error");
                } else {
                    LoginUser loginUser = JSONObject.parseObject(userToken,LoginUser.class);
                    request.setAttribute("loginUser",loginUser);
                }
            }else {
                throw new RuntimeException("Login error");
            }
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
