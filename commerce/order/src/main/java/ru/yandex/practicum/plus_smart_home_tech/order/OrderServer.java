package ru.yandex.practicum.plus_smart_home_tech.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.handler.ErrorHandler;

@SpringBootApplication(scanBasePackageClasses = {
        OrderServer.class,
        ErrorHandler.class
})
@EnableFeignClients(basePackages = "ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign")
public class OrderServer {
    public static void main(String[] args) {
        SpringApplication.run(OrderServer.class, args);
    }
}
