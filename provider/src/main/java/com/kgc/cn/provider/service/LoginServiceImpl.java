package com.kgc.cn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.kgc.cn.common.dto.Userdb;
import com.kgc.cn.common.dto.UserdbExample;
import com.kgc.cn.common.enums.ErrorEnums;
import com.kgc.cn.common.exception.ServiceException;
import com.kgc.cn.common.service.LoginService;
import com.kgc.cn.common.vo.SupportUserLoginVo;
import com.kgc.cn.provider.mapper.UserdbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserdbMapper userdbMapper;

    @Override
    public boolean toLogin(SupportUserLoginVo supportUserLoginVo) {
        UserdbExample userdbExample = new UserdbExample();
        userdbExample.createCriteria().andUsernameEqualTo(supportUserLoginVo.getUsername());
        List<Userdb> userdbList = userdbMapper.selectByExample(userdbExample);
        if (CollectionUtils.isEmpty(userdbList)) {
            Userdb userdb = userdbList.get(0);
            if (!ObjectUtils.isEmpty(userdb)) {
                //验证密码
                boolean isName = userdbList.get(0).getUsername().equals(supportUserLoginVo.getUsername());
                boolean isPassword = userdbList.get(0).getPassword().equals(supportUserLoginVo.getPassword());
                if (isName && isPassword) {
                    return true;
                }
                throw new ServiceException(ErrorEnums.EMPTY_USER);
            }
                throw new ServiceException(ErrorEnums.EMPTY_USER);
            }
        return false;
    }
}
