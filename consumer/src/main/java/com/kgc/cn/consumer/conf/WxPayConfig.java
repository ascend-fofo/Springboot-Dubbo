package com.kgc.cn.consumer.conf;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wxPayConfig")
public class WxPayConfig {
    private String appid;
    private String mch_id;
    private String notify_url;
    private String key;
    private String unifiedUrl;
}
