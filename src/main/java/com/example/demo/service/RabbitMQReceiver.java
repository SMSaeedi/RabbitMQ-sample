package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "${explore.rabbitmq.queue}")
public class RabbitMQReceiver {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQReceiver.class);

    @RabbitHandler
    public void receive(AccountDto accountDto) {
        log.info("Received account message: {}", accountDto);
    }

    @RabbitHandler
    public void receive(String message) {
        log.info("Received plain message: {}", message);
    }
}
