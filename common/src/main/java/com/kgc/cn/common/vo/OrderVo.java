package com.kgc.cn.common.vo;

import lombok.Data;

@Data
public class OrderVo {
    private int orderId;
    private int goodId;
    private String phone;
    private int userId;
    private int state;
}
