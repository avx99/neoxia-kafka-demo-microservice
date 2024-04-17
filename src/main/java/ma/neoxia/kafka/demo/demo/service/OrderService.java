package ma.neoxia.kafka.demo.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.neoxia.kafka.demo.demo.dto.CreateOrderDto;
import ma.neoxia.kafka.demo.demo.dto.OrderCreationResponse;
import ma.neoxia.kafka.demo.demo.enums.OrderStatus;
import ma.neoxia.kafka.demo.demo.exception.DomainException;
import ma.neoxia.kafka.demo.demo.mapper.AppMapper;
import ma.neoxia.kafka.demo.demo.repository.CustomerRepository;
import ma.neoxia.kafka.demo.demo.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final PaymentService paymentService;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, CreateOrderDto> kafkaTemplate;
    private final AppMapper appMapper;

    public void validateOrder(CreateOrderDto createOrderCommand) {
        var customer = customerRepository.findById(createOrderCommand.customerId());
        if (customer.isEmpty()) {
            throw new DomainException("user not found");
        }

        if (BigDecimal.ZERO.equals(createOrderCommand.price())) {
            throw new DomainException("price should be more the zero");
        }
    }

    public OrderCreationResponse getOrderByCustomer(UUID customerId) {
        var order = orderRepository.findByCustomerId(customerId).orElseThrow(() -> new DomainException("order not found !"));
        return new OrderCreationResponse(order.getId().toString(), "order created !", order.getOrderStatus());
    }


    public OrderCreationResponse createOrder(CreateOrderDto createOrderCommand) {
        validateOrder(createOrderCommand);
        paymentService.validatePayment(createOrderCommand);
        var order = orderRepository.save(appMapper.createOrderDtoToOrder(createOrderCommand));
        var future = kafkaTemplate.send("neoxia-ma-demo-request-payment-v1.0", createOrderCommand);
        future.whenCompleteAsync((result, ex) -> {
            log.info("done sending : {}", result.getProducerRecord().toString());
        });
        return new OrderCreationResponse(order.getId().toString(), "order created !", null);
    }

    @KafkaListener(
            topics = "neoxia-ma-demo-response-payment-v1.0",
            containerFactory = "createOrderKafkaListenerContainerFactory")
    public void paymentReceived(CreateOrderDto createOrderCommand) {
        log.info("receiving message", createOrderCommand.toString());
        var order = orderRepository.findByCustomerId(createOrderCommand.customerId()).orElseThrow(() -> new DomainException("order not found !"));
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);
        var future = kafkaTemplate.send("neoxia-ma-demo-request-delivery-v1.0", createOrderCommand);
        future.whenCompleteAsync((result, ex) -> {
            log.info("message sent to neoxia-ma-demo-request-delivery-v1.0");
        });
    }

    @KafkaListener(
            topics = "neoxia-ma-demo-response-delivery-v1.0",
            containerFactory = "createOrderKafkaListenerContainerFactory")
    public void deliveryDone(CreateOrderDto createOrderCommand) {
        log.info("receiving message {}", createOrderCommand.toString());
        var order = orderRepository.findByCustomerId(createOrderCommand.customerId()).orElseThrow(() -> new DomainException("order not found !"));
        order.setOrderStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
    }
}
