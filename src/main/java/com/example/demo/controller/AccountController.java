package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.enums.Currency;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @PostMapping("/{accountId}/{customerId}")
    public AccountDto createAccount(@PathVariable int accountId, @PathVariable int customerId, @RequestParam List<Currency> currencies) {
        return null;
    }

    @GetMapping("/{accountId}")
    public AccountDto getAccount(@PathVariable int accountId) {
        return null;
    }
}