package com.kgc.cn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;

import com.kgc.cn.common.dto.UserDto;
import com.kgc.cn.common.service.DubboTestService;
import com.kgc.cn.common.vo.UserVo;
import org.springframework.beans.BeanUtils;

@Service
public class DubboTestServiceImpl implements DubboTestService {
    @Override
    public void testDubbo() {
        if (1 == 1) {

        }
    }

    @Override
    public UserVo getUser() {
        UserVo userVo = new UserVo();
        userVo.setId(1);
        userVo.setName("ascendFoFo");
        userVo.setSex(1);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userVo, userDto);
        return userVo;
    }
}
