package com.example.demo;

import com.example.demo.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CoreBankingApplicationTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void contextLoads() {
        assertThat(customerRepository).isNotNull();
    }
}
