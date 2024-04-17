package ma.neoxia.kafka.demo.demo.mapper;

import ma.neoxia.kafka.demo.demo.domain.Delivery;
import ma.neoxia.kafka.demo.demo.domain.Order;
import ma.neoxia.kafka.demo.demo.domain.Payment;
import ma.neoxia.kafka.demo.demo.dto.CreateOrderDto;
import ma.neoxia.kafka.demo.demo.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppMapper {
    public Order createOrderDtoToOrder(CreateOrderDto createOrderDto) {
        return Order
                .builder()
                .customerId(createOrderDto.customerId())
                .price(createOrderDto.price())
                .orderStatus(OrderStatus.PENDING)
//                .address(OrderAddressEntity.builder().street(createOrderDto.address().street()).city(createOrderDto.address().city()).build())
                .build();
    }

    public Payment createOrderDtoToPayment(CreateOrderDto createOrderDto) {
        return Payment
                .builder()
                .customerId(createOrderDto.customerId())
                .orderId(createOrderDto.customerId())
                .amount(createOrderDto.price())
                .build();
    }

    public Delivery createOrderDtoToDelivery(CreateOrderDto createOrderDto, UUID orderId) {
        return Delivery
                .builder()
                .customerId(createOrderDto.customerId())
                .orderId(orderId)
                .address(createOrderDto.address().street())
                .build();
    }
}
