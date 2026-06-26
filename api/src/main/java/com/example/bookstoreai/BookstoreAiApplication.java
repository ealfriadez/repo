package com.example.bookstoreai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookstoreAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreAiApplication.class, args);
    }
}
