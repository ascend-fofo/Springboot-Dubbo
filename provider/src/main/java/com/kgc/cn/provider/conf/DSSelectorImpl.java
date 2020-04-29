package com.kgc.cn.provider.conf;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by xueqijun on 2020/4/16.
 */
@Component
@Aspect
public class DSSelectorImpl {
    @Before("com.kgc.cn.provider.conf.DsPointcut.selectDsPointcut()")
    public void chageDS(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DSSelector selector = method.getAnnotation(DSSelector.class);
        if (selector==null) return;

        MultipleDataSourceHelper.set(selector.value());
    }
}
