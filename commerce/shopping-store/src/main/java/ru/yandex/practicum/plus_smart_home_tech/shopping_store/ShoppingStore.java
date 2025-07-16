package ru.yandex.practicum.plus_smart_home_tech.shopping_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ShoppingStore {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingStore.class, args);
    }
}