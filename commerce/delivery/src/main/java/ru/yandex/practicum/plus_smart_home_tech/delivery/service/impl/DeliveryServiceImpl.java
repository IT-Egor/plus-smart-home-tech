package ru.yandex.practicum.plus_smart_home_tech.delivery.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.plus_smart_home_tech.delivery.config.DeliveryCostRates;
import ru.yandex.practicum.plus_smart_home_tech.delivery.dao.DeliveryRepository;
import ru.yandex.practicum.plus_smart_home_tech.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.plus_smart_home_tech.delivery.model.Address;
import ru.yandex.practicum.plus_smart_home_tech.delivery.model.Delivery;
import ru.yandex.practicum.plus_smart_home_tech.delivery.service.DeliveryService;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.order.OrderDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.dto.warehouse.ShipToDeliveryRequestDto;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.enums.delivery.DeliveryState;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.exception.NotFoundException;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.OrderFeign;
import ru.yandex.practicum.plus_smart_home_tech.interaction_api.feign.WarehouseFeign;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderFeign orderFeign;
    private final WarehouseFeign warehouseFeign;
    private final DeliveryCostRates deliveryCostRates;

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

    @Override
    public void setPicked(UUID orderId) {
        Delivery delivery = findDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        ShipToDeliveryRequestDto request = new ShipToDeliveryRequestDto(orderId, delivery.getDeliveryId());
        warehouseFeign.shipToDelivery(request);
        orderFeign.assembly(delivery.getOrderId());
        deliveryRepository.save(delivery);
    }

    @Override
    public void setFailed(UUID orderId) {
        Delivery delivery = findDeliveryByOrderId(orderId);
        orderFeign.complete(delivery.getOrderId());
        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
    }

    @Override
    public BigDecimal calculateDeliveryCost(OrderDto orderDto) {
        Delivery delivery = findDeliveryById(orderDto.getDeliveryId());
        Address fromAddress = delivery.getFromAddress();
        Address toAddress = delivery.getToAddress();

        BigDecimal totalCost = deliveryCostRates.getBase();

        BigDecimal cityMultiplier = fromAddress.getCity().equals("ADDRESS_1")
                ? deliveryCostRates.getAddress1()
                : deliveryCostRates.getAddress2();
        totalCost = totalCost.add(totalCost.multiply(cityMultiplier));

        if (orderDto.isFragile()) {
            totalCost = totalCost.add(totalCost.multiply(deliveryCostRates.getFragile()));
        }

        totalCost = totalCost.add(
                BigDecimal.valueOf(orderDto.getDeliveryWeight()).multiply(deliveryCostRates.getWeight())
        );
        totalCost = totalCost.add(
                BigDecimal.valueOf(orderDto.getDeliveryVolume()).multiply(deliveryCostRates.getVolume())
        );

        if (!fromAddress.getStreet().equals(toAddress.getStreet())) {
            totalCost = totalCost.add(totalCost.multiply(deliveryCostRates.getStreet()));
        }

        return totalCost;
    }

    private Delivery findDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Delivery for order `%s` not found".formatted(orderId)));
    }

    private Delivery findDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery `%s` not found".formatted(deliveryId)));
    }
}
