package com.kgc.cn.common.dto;

import lombok.Data;

@Data
public class OrderDto {
    private int orderId;
    private int goodId;
    private String phone;
    private int userId;
    private int state;
}
