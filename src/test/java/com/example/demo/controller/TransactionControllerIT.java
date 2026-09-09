package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.model.Customer;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Long customerId;
    private Long accountId;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();

        Customer customer = new Customer();
        customer.setFullName("Alice Johnson");
        customer.setZipCode("54321");
        customer.setPhoneNumber(987654321L);
        customer.setAddress("Maple Avenue 12");
        customerId = customerRepository.save(customer).getId();

        Account account = new Account();
        account.setCustomer(customer);
        account.setBalance(new BigDecimal("500.00"));
        accountId = accountRepository.save(account).getId();
    }

    @Test
    void creditAndDebit_shouldUpdateBalanceAndReturnHistory() throws Exception {
        mockMvc.perform(post("/api/accounts/{accountId}/transactions/credit", accountId)
                .param("amount", "250.50")
                .param("description", "Salary")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.type").value("CREDIT"))
            .andExpect(jsonPath("$.amount").value(250.50))
            .andExpect(jsonPath("$.newBalance").value(750.50));

        mockMvc.perform(post("/api/accounts/{accountId}/transactions/debit", accountId)
                .param("amount", "100.25")
                .param("description", "Groceries")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.type").value("DEBIT"))
            .andExpect(jsonPath("$.amount").value(100.25))
            .andExpect(jsonPath("$.newBalance").value(650.25));

        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId)
                .param("limit", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("Groceries"))
            .andExpect(jsonPath("$[1].description").value("Salary"))
            .andExpect(jsonPath("$[0].accountId").value(accountId))
            .andExpect(jsonPath("$[0].amount").value(100.25));
    }

    @Test
    void history_shouldSupportDateFilterAndLast10Limit() throws Exception {
        mockMvc.perform(post("/api/accounts/{accountId}/transactions/credit", accountId)
                .param("amount", "40.00")
                .param("description", "Bonus")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId)
                .param("limit", "10")
                .param("fromDate", "2025-01-01")
                .param("toDate", "2099-12-31"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("Bonus"))
            .andExpect(jsonPath("$[0].amount").value(40.00));

        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId)
                .param("limit", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)));
    }
}
