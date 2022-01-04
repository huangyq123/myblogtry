package com.example.myblogtry;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.myblogtry.dao")
public class MyblogtryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyblogtryApplication.class, args);
    }

}
