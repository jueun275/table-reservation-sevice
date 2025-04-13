package com.example.tablereservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class TableReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TableReservationServiceApplication.class, args);
    }

}
