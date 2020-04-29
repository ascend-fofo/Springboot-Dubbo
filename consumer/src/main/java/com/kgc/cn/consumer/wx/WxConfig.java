package com.kgc.cn.consumer.wx;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxConfig {
    private String appId;
    private String redirectUri;
    private String responseType;
    private String scope;
    private String codeUrl;
    private String accessTokenUri;
    private String secret;
    private String grantType;
    private String userInfoUri;
    private String loginSuccess;

    public String reqCodeUri(){
        StringBuffer wxUri = new StringBuffer(getCodeUrl());
        wxUri.append("?").append("appid=").append(getAppId());
        wxUri.append("&").append("redirect_uri=").append(getRedirectUri());
        wxUri.append("&").append("response_type=").append(getResponseType());
        wxUri.append("&").append("scope=").append(getScope());
        wxUri.append("&").append("state=").append("STATE");
        wxUri.append("#wechat_redirect");
        return wxUri.toString();
    }

    public String reqAccessTokenUri(String code){
        StringBuffer sb = new StringBuffer(getAccessTokenUri());
        sb.append("?").append("appid=").append(getAppId());
        sb.append("&").append("secret=").append(getSecret());
        sb.append("&").append("code=").append(code);
        sb.append("&").append("grant_type=").append(getGrantType());
        return sb.toString();
    }

    public String reqUserInfoUri(String accessTokenUri,String openId) {
        StringBuffer sb = new StringBuffer(getUserInfoUri());
        sb.append("?").append("access_token=").append(accessTokenUri);
        sb.append("&").append("openid=").append(openId).append("&lang=zh_CN");
        return sb.toString();
    }
}
