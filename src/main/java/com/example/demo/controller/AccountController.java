package com.example.demo.controller;

import com.example.demo.dto.AccountDto;
import com.example.demo.enums.AccountType;
import com.example.demo.enums.Currency;
import com.example.demo.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(
        @RequestParam Long customerId,
        @RequestParam(required = false, defaultValue = "0") BigDecimal balance,
        @RequestParam(required = false, defaultValue = "USD") Currency currency,
        @RequestParam(required = false, defaultValue = "SAVINGS") AccountType accountType
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(accountService.createAccount(customerId, balance, currency, accountType));
    }

    @GetMapping("/{accountId}")
    public AccountDto getAccount(@PathVariable Long accountId) {
        return accountService.getById(accountId);
    }

    @GetMapping("/customer/{customerId}")
    public List<AccountDto> getAccountsByCustomer(@PathVariable Long customerId) {
        return accountService.getByCustomerId(customerId);
    }
}
