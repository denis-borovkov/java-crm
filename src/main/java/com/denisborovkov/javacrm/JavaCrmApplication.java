package com.denisborovkov.javacrm;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaCrmApplication {
    public static void main(String[] args) {
        Dotenv.configure().filename("keys.env").ignoreIfMissing().load()
                .entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        SpringApplication.run(JavaCrmApplication.class, args);
    }
}



