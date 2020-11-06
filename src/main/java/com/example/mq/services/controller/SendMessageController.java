package com.example.mq.services.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: YHC
 * Date: 2020/11/6
 * DESC:
 */
@RestController
public class SendMessageController {

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @GetMapping("/dead")
    public String deadLetterMessage(@RequestParam("msg") String msg){
        String BUSINESS_EXCHANGE_NAME = "dead.letter.demo.simple.business.exchange";

        rabbitTemplate.convertSendAndReceive(BUSINESS_EXCHANGE_NAME, "", msg);

        return "OK";
    }
}
