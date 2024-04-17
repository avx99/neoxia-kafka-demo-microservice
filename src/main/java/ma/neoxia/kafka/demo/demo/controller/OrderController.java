package ma.neoxia.kafka.demo.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.neoxia.kafka.demo.demo.dto.CreateOrderDto;
import ma.neoxia.kafka.demo.demo.dto.OrderCreationResponse;
import ma.neoxia.kafka.demo.demo.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService OrderService;


    @PostMapping
    public OrderCreationResponse createOrder(@RequestBody CreateOrderDto createOrderCommand) {
        log.info("Creating order for customer: {} ", createOrderCommand.customerId());
        return OrderService.createOrder(createOrderCommand);
    }

    @GetMapping
    public OrderCreationResponse getOrderByCustomer(@RequestParam("customer_id") UUID customerId) {
        log.info("getting order for customer: {} ", customerId.toString());
        return OrderService.getOrderByCustomer(customerId);
    }



}
