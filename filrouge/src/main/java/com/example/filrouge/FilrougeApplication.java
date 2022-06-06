package com.example.filrouge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ComponentScan("com.example.filrouge.dto")
public class FilrougeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilrougeApplication.class, args);
    }

}
