package com.kgc.cn.provider.conf;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 实现数据源路由
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return MultipleDataSourceHelper.get();
    }
}
