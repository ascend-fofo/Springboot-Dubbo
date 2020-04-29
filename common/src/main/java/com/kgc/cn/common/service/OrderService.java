package com.kgc.cn.common.service;

import com.kgc.cn.common.dto.LoginUser;

public interface OrderService {

    boolean add(LoginUser loginUser, int goodId);
}
