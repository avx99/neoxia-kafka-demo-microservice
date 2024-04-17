package ma.neoxia.kafka.demo.demo.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateOrderDto(UUID customerId, BigDecimal price,
                             List<OrderItem> items, OrderAddress address) {
}
