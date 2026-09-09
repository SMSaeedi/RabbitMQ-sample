package com.example.demo.controller;

import com.example.demo.dto.TransactionDto;
import com.example.demo.enums.TransactionType;
import com.example.demo.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/accounts/{accountId}/transactions/credit")
    public ResponseEntity<TransactionDto> credit(
        @PathVariable Long accountId,
        @RequestParam BigDecimal amount,
        @RequestParam(required = false) String description
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(transactionService.credit(accountId, amount, description));
    }

    @PostMapping("/accounts/{accountId}/transactions/debit")
    public ResponseEntity<TransactionDto> debit(
        @PathVariable Long accountId,
        @RequestParam BigDecimal amount,
        @RequestParam(required = false) String description
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(transactionService.debit(accountId, amount, description));
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<TransactionDto> getHistory(
        @PathVariable Long accountId,
        @RequestParam(required = false, defaultValue = "10") Integer limit,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return transactionService.getHistory(accountId, limit, fromDate, toDate);
    }

    @GetMapping("/transactions/{transactionId}")
    public TransactionDto getTransaction(@PathVariable Long transactionId) {
        return transactionService.getById(transactionId);
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionDto> createTransaction(
        @RequestParam Long accountId,
        @RequestParam BigDecimal amount,
        @RequestParam TransactionType type,
        @RequestParam(required = false) String description
    ) {
        if (type == TransactionType.CREDIT) {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.credit(accountId, amount, description));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(transactionService.debit(accountId, amount, description));
    }

    @GetMapping("/transactions/{accountId}/history")
    public List<TransactionDto> transactionsHistory(
        @PathVariable Long accountId,
        @RequestParam(required = false, defaultValue = "10") Integer limit,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return transactionService.getHistory(accountId, limit, fromDate, toDate);
    }
}