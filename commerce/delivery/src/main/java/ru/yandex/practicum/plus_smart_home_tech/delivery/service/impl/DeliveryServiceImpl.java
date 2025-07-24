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

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        delivery.setDeliveryState(DeliveryState.CREATED);
        return deliveryMapper.toDto(deliveryRepository.save(delivery));
    }
}
