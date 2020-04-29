package com.kgc.cn.consumer.controller;


import com.kgc.cn.common.enums.LightEnum;
import com.kgc.cn.consumer.utils.ActiveMQUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/send")
public class ActiveMQController {
    @Autowired
    private ActiveMQUtils activeMQUtils;


    @GetMapping(value = "/sendMsg")
    public void sendMsg(@RequestParam String msg) {
        activeMQUtils.sendQueueMsg("test", msg);
    }

    @JmsListener(destination = "test")
    public String getMsg(String msg){
        return msg;
    }

    @GetMapping(value = "/sendTopicMsg")
    public void sendTopicMsg(@RequestParam String msg) {
        activeMQUtils.sendTopicMsg("topic1", msg);
    }

    @JmsListener(destination = "topic1")
    public void getTopicMsg1(String msg){
        System.out.println(msg+"1");
    }

    @JmsListener(destination = "topic1")
    public void getTopicMsg2(String msg){
        System.out.println(msg+"2");
    }

    @GetMapping(value = "/light")
    public String light(String opStr){
        String whichLight = "";
        switch (LightEnum.matchCode(opStr)){
            case RED:
                System.out.println("红灯");
                whichLight = "红灯";
                break;
            case GREEN:
                System.out.println("绿灯");
                whichLight = "绿灯";
                break;
            case YELLOW:
                System.out.println("黄灯");
                whichLight = "黄灯";
                break;
        }
        return whichLight;
    }

}
