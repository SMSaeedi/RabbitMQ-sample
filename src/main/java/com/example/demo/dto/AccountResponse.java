package com.example.demo.dto;

import com.example.demo.model.Balance;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountResponse {
    private int accountId;
    private int customerId;
    private List<Balance> balances;
}
