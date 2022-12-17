package com.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final ObjectMapper mapper;

    public AccountService(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
