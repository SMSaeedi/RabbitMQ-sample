package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.enums.Currency;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {
    private final   RabbitTemplate rabbitTemplate;
    @PostMapping("/{accountId}/{customerId}")
    public AccountDto createAccount(@PathVariable int accountId, @PathVariable int customerId, @RequestParam List<Currency> currencies) {
        return null;
    }

    @GetMapping("/{accountId}")
    public AccountDto getAccount(@PathVariable int accountId) {
        return null;
    }
}