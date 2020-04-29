package com.kgc.cn.common.utils;

import com.kgc.cn.common.dto.Order;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;


@Component
public class ActiveMQUtils {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendQueueMsg(String name, Object msg){
        Destination destination = new ActiveMQQueue(name);
        jmsMessagingTemplate.convertAndSend(destination,msg);
    }

    public void sendTopicMsg(String name, Object msg){
        Destination destination = new ActiveMQTopic(name);
        jmsMessagingTemplate.convertAndSend(destination,msg);
    }

    public void sendQueueOrder(String name,Order order){
        Destination destination = new ActiveMQQueue(name);
        jmsMessagingTemplate.convertAndSend(destination,order);
    }
}
