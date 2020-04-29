package com.kgc.cn.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SupportUserLoginVo implements Serializable {
    private static final long serialVersionUID = -4303851033643029166L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

}
