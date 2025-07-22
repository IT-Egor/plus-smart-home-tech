package ru.yandex.practicum.plus_smart_home_tech.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign")
public class WarehouseServer {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseServer.class, args);
    }
}
