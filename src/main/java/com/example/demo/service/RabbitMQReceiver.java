package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
@Service
@RabbitListener(queues = "${explore.rabbitmq.queue}")
@Slf4j
public class RabbitMQReceiver {

    @RabbitHandler
    public void receiver(AccountDto accountDto){
        log.info(accountDto.toString());
    }

    @RabbitHandler
    public void receiver(String message){
        log.info(message);
    }




}

