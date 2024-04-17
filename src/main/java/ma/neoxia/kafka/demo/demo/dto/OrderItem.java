package ma.neoxia.kafka.demo.demo.dto;

import lombok.Builder;


import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItem(UUID productId, Integer quantity, BigDecimal price,
                        BigDecimal subTotal) {
}
