package ru.yandex.practicum.plus_smart_home_tech.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class WarehouseServer {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseServer.class, args);
    }
}
