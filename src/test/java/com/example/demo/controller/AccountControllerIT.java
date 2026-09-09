package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private Long customerId;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        Customer customer = new Customer();
        customer.setFullName("Alice Johnson");
        customer.setZipCode("54321");
        customer.setPhoneNumber(123456789L);
        customer.setAddress("Maple Avenue 12");
        customerId = customerRepository.save(customer).getId();
    }

    @Test
    void createAccount_shouldReturnCreatedAccount() throws Exception {
        mockMvc.perform(post("/api/accounts")
                .param("customerId", String.valueOf(customerId))
                .param("balance", "1200.50")
                .param("currency", "USD")
                .param("accountType", "SAVINGS")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.customerId").value(customerId))
            .andExpect(jsonPath("$.balance").value(1200.50))
            .andExpect(jsonPath("$.currency").value("USD"))
            .andExpect(jsonPath("$.accountType").value("SAVINGS"));

        assertThat(customerRepository.findById(customerId)).isPresent();
    }

    @Test
    void getAccountsByCustomer_shouldReturnAllCustomerAccounts() throws Exception {
        mockMvc.perform(post("/api/accounts")
                .param("customerId", String.valueOf(customerId))
                .param("balance", "350.00")
                .param("currency", "EUR")
                .param("accountType", "SALARY"))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/accounts/customer/{customerId}", customerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].currency").value("EUR"))
            .andExpect(jsonPath("$[0].accountType").value("SALARY"));
    }
}
