package com.kgc.cn.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {
    OBJ_NOT_FOUND(1,"对象找不到异常"),
    EMPTY_PARAM(2,"参数为空异常");
    private Integer code;
    private String message;

}
