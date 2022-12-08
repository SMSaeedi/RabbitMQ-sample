package com.example.demo.config;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ConfigStart implements CommandLineRunner {

//    @Value("classPath:data.json")
//   private String importFile;

    @Autowired
    private CustomerService customerService;

    @Override
    public void run(String... args) throws Exception {

    }

    private void createCustomer(){

    }
}