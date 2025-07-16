package ru.yandex.practicum.plus_smart_home_tech.shopping_cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ShoppingCartServer {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartServer.class, args);
    }
}