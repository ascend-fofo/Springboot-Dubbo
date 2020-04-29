package com.kgc.cn.provider.conf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.kgc.cn.provider.mapper")
public class MybatisConfigure {
}
