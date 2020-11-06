package com.example.mq.services.receiver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.support.RabbitExceptionTranslator;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: YHC
 * Date: 2020/11/5
 * DESC: 手动确认消息接收
 */
@Component
@RabbitListener(queues = "TestManualDirectQueue")
public class ManualDirectReceiver implements ChannelAwareMessageListener {

//    @Override
//    public void onMessage(Message message, Channel channel) throws Exception {
//
//        System.out.println(message.toString());
//
//        Thread.sleep(5000); //用于测试重回队列
//
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        try {
//            //因为传递消息的时候用的map传递,所以将Map从Message内取出需要做些处理
//            String msg = message.toString();
//            String[] msgArray = msg.split("'");//可以点进Message里面看源码,单引号直接的数据就是我们的map消息数据
//            Map<String, String> msgMap = mapStringToMap(msgArray[1].trim());
//            String messageId=msgMap.get("messageId");
//            String messageData=msgMap.get("messageData");
//            String createTime=msgMap.get("createTime");
//            System.out.println("ManualMessageId:"+messageId+"  ManualMessageData:"+messageData+"  ManualCreateTime:"+createTime);
////            channel.basicAck(deliveryTag, true);
//
////			channel.basicReject(deliveryTag, true);//为true会重新放回队列|消息回到队列头部
//
//        } catch (Exception e) {
//            channel.basicReject(deliveryTag, false);
//            e.printStackTrace();
//        }
//    }

    //{key=value,key=value,key=value} 格式转换成map
    private Map<String, String> mapStringToMap(String str) {
        str = str.substring(1, str.length() - 1);
        String[] strs = str.split(",");
        Map<String, String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key = string.split("=")[0].trim();
            String value = string.split("=")[1];
            map.put(key, value);
        }
        return map;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        System.out.println(message.toString());

        Thread.sleep(5000); //用于测试重回队列

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String msg = message.toString();
            String[] msgArray = msg.split("'");//可以点进Message里面看源码,单引号直接的数据就是我们的map消息数据
            JSONObject jsonObject = JSONObject.parseObject(msgArray[1].trim());
            Map<String, Object> map = (Map<String, Object>)jsonObject;

            String messageId=(String) map.get("messageId");
            String messageData=(String) map.get("messageData");
            String createTime=(String) map.get("createTime");
            System.out.println("ManualMessageId:"+messageId+"  ManualMessageData:"+messageData+"  ManualCreateTime:"+createTime);

            channel.basicAck(deliveryTag, true);

            /*
             * 以下方式 消息回到队列尾部
             */
//            //手动进行应答
//            channel.basicAck(deliveryTag, false);
//            //重新发送消息到队尾
//            String exchange = message.getMessageProperties().getReceivedExchange();
//            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
//            //TEXT_PLAIN消息持久化
//            channel.basicPublish(exchange, routingKey, MessageProperties.TEXT_PLAIN, JSON.toJSONBytes(jsonObject));
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }

}
