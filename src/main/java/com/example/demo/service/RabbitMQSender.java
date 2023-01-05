package com.example.demo.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.slf4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("explore.rabbitmq.exchange")
    private String exchange;

    @Value("explore.rabbitmq.routing-key")
    private String routingKey;

    public void send(String  message){
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
        log.debug("Printing variable value: ".concat(message));
    }

}
