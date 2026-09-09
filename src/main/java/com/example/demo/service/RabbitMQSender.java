package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.enums.AccountType;
import com.example.demo.enums.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RabbitMQSender {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQSender.class);
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public RabbitMQSender(
        RabbitTemplate rabbitTemplate,
        @Value("${explore.rabbitmq.exchange}") String exchange,
        @Value("${explore.rabbitmq.routing-key}") String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void send(String message) {
        AccountDto accountDto = new AccountDto(null, null, BigDecimal.ZERO, Currency.USD, AccountType.SALARY);
        rabbitTemplate.convertAndSend(exchange, routingKey, accountDto);
        log.info("Sent message to RabbitMQ: {}", message);
    }
}
