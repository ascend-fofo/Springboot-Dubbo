package com.kgc.cn.consumer.service;

import com.kgc.cn.common.utils.CommonUtils;
import com.kgc.cn.common.utils.HttpClientUtils;
import com.kgc.cn.common.utils.WxPayUtils;
import com.kgc.cn.common.vo.OrderVo;
import com.kgc.cn.consumer.conf.WxPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class TestService {
    @Autowired
    private WxPayConfig wxPayConfig;

    public void isOrder() {
        OrderVo orderVo = new OrderVo();
        unifiedOrder(orderVo);
    }

    //统一下单方法
    private String unifiedOrder(OrderVo orderVo) {
        SortedMap<String,String> param = new TreeMap<>();
        //公众账号ID
        param.put("appid",wxPayConfig.getAppid());
        //商户号
        param.put("mch_id",wxPayConfig.getMch_id());
        //随机字符串
        param.put("nonce_str", CommonUtils.generatorUUID());
        //商品描述
        param.put("body",orderVo.getPhone());
        //商户订单号
        param.put("out_trade_no",String.valueOf(orderVo.getOrderId()));
        //订单金额
        param.put("total_fee","1");
        //终端ip
        param.put("spbill_create_ip","192.168.31.211");
        //通知地址
        param.put("notify_url",wxPayConfig.getNotify_url());
        //交易类型
        param.put("trade_type","NATIVE");

        //签名
        param.put("sign", WxPayUtils.generateSignature(param,wxPayConfig.getKey()));
        String payXml = WxPayUtils.mapToXml(param);

        //请求统一下单接口
        String order = HttpClientUtils.doPost(wxPayConfig.getUnifiedUrl(),payXml,4000);
        Map<String,String> resultMap = WxPayUtils.xmlToMap(order);
        if (null != resultMap) {
            String codeUrl = resultMap.get("code_url");
            if (null != codeUrl) {

            }
        }

        return null;
    }
}
