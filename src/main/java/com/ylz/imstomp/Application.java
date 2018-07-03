package com.ylz.imstomp;

import com.ylz.imstomp.service.StorageService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.ylz", "com.hxg"})
@MapperScan(basePackages = "com.ylz.**.mapper")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(final StorageService storageService) {
        return args -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}