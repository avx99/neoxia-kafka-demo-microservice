package ma.neoxia.kafka.demo.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.neoxia.kafka.demo.demo.domain.Delivery;
import ma.neoxia.kafka.demo.demo.dto.CreateOrderDto;
import ma.neoxia.kafka.demo.demo.enums.OrderStatus;
import ma.neoxia.kafka.demo.demo.exception.DomainException;
import ma.neoxia.kafka.demo.demo.mapper.AppMapper;
import ma.neoxia.kafka.demo.demo.repository.DeliveryRepository;
import ma.neoxia.kafka.demo.demo.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, CreateOrderDto> kafkaTemplate;
    private final AppMapper appMapper;

    public Delivery createDelivery(CreateOrderDto createOrderDto, UUID orderId) {
        var payment = appMapper.createOrderDtoToDelivery(createOrderDto, orderId);
        return deliveryRepository.save(payment);
    }

    @KafkaListener(
            topics = "neoxia-ma-demo-request-delivery-v1.0",
            containerFactory = "createOrderKafkaListenerContainerFactory")
    public void prepareForDeliveryOperation(CreateOrderDto createOrderCommand) throws InterruptedException {
        log.info("receiving message {}", createOrderCommand.toString());
        Thread.sleep(10000L);
        var order = orderRepository.findByCustomerId(createOrderCommand.customerId()).orElseThrow(() -> new DomainException("order not found !"));
        createDelivery(createOrderCommand, order.getId());
        order.setOrderStatus(OrderStatus.IN_DELIVERY);
        orderRepository.save(order);
        var future = kafkaTemplate.send("neoxia-ma-demo-response-delivery-v1.0", createOrderCommand);
        future.whenCompleteAsync((result, ex) -> {
            log.info("message sent to neoxia-ma-demo-response-delivery-v1.0");
        });
    }
}
