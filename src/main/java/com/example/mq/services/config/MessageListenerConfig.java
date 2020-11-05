package com.example.mq.services.config;

import com.example.mq.services.receiver.ManualDirectReceiver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author :
 * @CreateTime : 2019/9/4
 * @Description : 收到消息的手动确认
 **/
@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private ManualDirectReceiver manualDirectReceiver;//Direct消息接收处理类|自定义类
//    @Autowired
//    FanoutReceiverA fanoutReceiverA;//Fanout消息接收处理类A
//    @Autowired
//    DirectRabbitConfig directRabbitConfig;

    @Autowired
    ManualDirectRabbitConfig manualDirectRabbitConfig; //自定义类

//    @Autowired
//    FanoutRabbitConfig fanoutRabbitConfig;
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // RabbitMQ默认是自动确认，这里改为手动确认消息
        container.setQueues(manualDirectRabbitConfig.TestManualDirectQueue()); //设置监听队列
        container.setMessageListener(manualDirectReceiver); //设置监听
//        container.addQueues(fanoutRabbitConfig.queueA());
//        container.setMessageListener(fanoutReceiverA);
        return container;
    }
}
