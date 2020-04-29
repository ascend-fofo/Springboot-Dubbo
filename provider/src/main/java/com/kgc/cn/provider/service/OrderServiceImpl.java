package com.kgc.cn.provider.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.kgc.cn.common.dto.LoginUser;
import com.kgc.cn.common.dto.Order;
import com.kgc.cn.common.service.OrderService;
import com.kgc.cn.common.utils.RedisUtils;
import com.kgc.cn.provider.mapper.FlashgoodsMapper;
import com.kgc.cn.provider.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private FlashgoodsMapper flashgoodsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean add(LoginUser loginUser, int goodId) {
        Order order = new Order();
        order.setGoodid(goodId);
        order.setUserid(loginUser.getId());
        order.setPhone(loginUser.getName());

        try {
            flashgoodsMapper.delGoodsNum(1,goodId);
            orderMapper.insertSelective(order);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
