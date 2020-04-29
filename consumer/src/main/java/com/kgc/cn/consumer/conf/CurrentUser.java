package com.kgc.cn.consumer.conf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)//代表我这个自定义注解要放在形参中
@Retention(RetentionPolicy.RUNTIME)//运行时有效
public @interface CurrentUser {
}
