package ru.yandex.practicum.plus_smart_home_tech.delivery.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.delivery.dao.DeliveryRepository;
import ru.yandex.practicum.plus_smart_home_tech.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.plus_smart_home_tech.delivery.model.Delivery;
import ru.yandex.practicum.plus_smart_home_tech.delivery.service.DeliveryService;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryState;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotFoundException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.OrderFeign;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderFeign orderFeign;

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        delivery.setDeliveryState(DeliveryState.CREATED);
        return deliveryMapper.toDto(deliveryRepository.save(delivery));
    }

    @Override
    public void setSuccessful(UUID orderId) {
        Delivery delivery = findDeliveryByOrderId(orderId);
        orderFeign.complete(delivery.getOrderId());
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
    }

    private Delivery findDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Delivery for order `%s` not found".formatted(orderId)));
    }
}
