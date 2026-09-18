package com.prakritikishore.carparkservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarparkServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarparkServiceApplication.class, args);
    }

}
