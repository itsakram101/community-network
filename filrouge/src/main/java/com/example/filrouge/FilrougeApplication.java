package com.example.filrouge;

import com.example.filrouge.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAsync
@SpringBootApplication
@EnableWebMvc
@Import(SwaggerConfiguration.class)
public class FilrougeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilrougeApplication.class, args);
    }

}
