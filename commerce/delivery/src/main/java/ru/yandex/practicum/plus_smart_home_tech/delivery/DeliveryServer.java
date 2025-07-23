package ru.yandex.practicum.plus_smart_home_tech.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.handler.ErrorHandler;

@SpringBootApplication(scanBasePackageClasses = {
        DeliveryServer.class,
        ErrorHandler.class
})
@EnableFeignClients(basePackages = "ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign")
public class DeliveryServer {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryServer.class, args);
    }
}
