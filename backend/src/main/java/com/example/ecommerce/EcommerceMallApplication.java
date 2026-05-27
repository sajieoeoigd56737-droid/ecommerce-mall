package com.example.ecommerce;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.ecommerce.mapper")
@SpringBootApplication
public class EcommerceMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceMallApplication.class, args);
    }
}
