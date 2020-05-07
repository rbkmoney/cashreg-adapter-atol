package com.rbkmoney.adapter.atol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class CashregAtolApplication extends SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(CashregAtolApplication.class, args);
    }
}
