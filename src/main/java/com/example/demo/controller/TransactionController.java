package com.example.demo.controller;

import com.example.demo.dto.TransactionRequest;
import com.example.demo.dto.TransactionResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @PostMapping
    public TransactionResponse createTransaction(@RequestBody TransactionRequest request) {
        return null;
    }

    @GetMapping("/{accountId}")
    public TransactionResponse getTransaction(@PathVariable int accountId) {
        return null;
    }

    @GetMapping("/{accountId}")
    public List<TransactionResponse> transactionsHistory(@PathVariable int accountId, @RequestParam int transactionCount, @RequestParam Date fromDate, @RequestParam Date toDate) {
        return null;
    }
}