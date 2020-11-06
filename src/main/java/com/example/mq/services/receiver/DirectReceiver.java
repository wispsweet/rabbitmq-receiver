package com.example.mq.services.receiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
    }

//    @RabbitHandler //可用于手动消息确认
//    public void process(Map testMessage, Channel channel, Message message) {
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("DirectReceiver消费者收到消息  : " + testMessage.toString());
//
//        try {
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
