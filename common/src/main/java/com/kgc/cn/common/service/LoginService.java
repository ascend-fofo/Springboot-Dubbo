package com.kgc.cn.common.service;

import com.kgc.cn.common.vo.SupportUserLoginVo;


public interface LoginService {
    /**
     * 登录
     * @param supportUserLoginVo
     * @return
     */
     boolean toLogin(SupportUserLoginVo supportUserLoginVo);
}
