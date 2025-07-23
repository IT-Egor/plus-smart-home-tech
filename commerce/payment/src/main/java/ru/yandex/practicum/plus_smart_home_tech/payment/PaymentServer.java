package ru.yandex.practicum.plus_smart_home_tech.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.handler.ErrorHandler;

@SpringBootApplication(scanBasePackageClasses = {
        PaymentServer.class,
        ErrorHandler.class
})
@EnableFeignClients(basePackages = "ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign")
public class PaymentServer {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServer.class, args);
    }
}
